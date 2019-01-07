(ns jackdaw.test.commands.base)

(def command-map
  {::stop (constantly true)

   ::sleep (fn [machine cmd [sleep-ms]]
             (Thread/sleep sleep-ms))

   ::println (fn [machine cmd params]
               (println (apply str params)))

   ::do (fn [machine cmd [do-fn]]
          (do-fn @(:journal machine)))})
