(ns toyrobot)

(defn transform [robot command]
  (case command
        "MOVE" (println "move")
        "LEFT" (println "left")
        "RIGHT" (println "right")
        "REPORT" (println "report")
        (println "other")))

(with-open [r (clojure.java.io/reader "examples/example1.txt")]
  (println (reduce transform [] (line-seq r))))
