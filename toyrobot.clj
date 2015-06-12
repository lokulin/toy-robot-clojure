#!/usr/bin/clojure

(ns toyrobot)

(defn table []
  {:llc {:x 0 :y 0}
   :urc {:x 4 :y 4}})

(defn robot []
  {:location  {:x 0 :y 0}
   :facing 0.5
   :table nil})

(defn le [lhs rhs]
  (and (<= (:x lhs) (:x rhs)) (<= (:y lhs) (:y rhs))))

(defn ge [lhs rhs]
  (and (>= (:x lhs) (:x rhs)) (>= (:y lhs) (:y rhs))))

(defn ontable [table location]
  (and (not (nil? table)) (le (:llc table) location) (ge (:urc table) location)))

(defn newmove [robot]
  {:x (+ (:x (:location robot)) (Math/sin (* Math/PI (:facing robot))))
   :y (+ (:y (:location robot)) (Math/cos (* Math/PI (:facing robot))))})

(defn move [robot]
  (let [newlocation (newmove robot)]
    (if (ontable (:table robot) newlocation)
      (assoc robot :location newlocation)
      robot)))

(defn place [robot newlocation dir table]
  (if (ontable table newlocation)
    (assoc robot :location newlocation :table table)
    robot))

(defn transform [robot command]
  (case command
        "MOVE" (move robot)
        "LEFT" (assoc robot :facing (mod (- (:facing robot) 0.5) 2))
        "RIGHT" (assoc robot :facing (mod (+ (:facing robot) 0.5) 2))
        "REPORT" (do (println robot) robot)
        (let [[_ x y dir] (re-matches #"PLACE (\d),(\d),(NORTH|EAST|SOUTH|WEST)" command)]
          (if (not (nil? dir))
            (place robot {:x (read-string x) :y (read-string y)} dir (table))
            robot))))

(with-open [r (clojure.java.io/reader "examples/example5.txt")]
  (reduce transform (robot) (line-seq r)))
