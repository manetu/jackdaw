(ns jackdaw.test.kafka
  (:require
   [clojure.java.io :as io]
   [jackdaw.test.fs :as fs])
  (:import
   (kafka.server KafkaConfig
                 KafkaServerStartable)))

(defn start!
  "Starts a kakfa broker.

   Returns a map containing the broker instance itself and a latch
   that waits until the broker is shutdown"
  [{:keys [config]}]
  (let [props (java.util.Properties.) ]
    (.putAll props config)
    (let [broker (-> props
                     (KafkaConfig.)
                     (KafkaServerStartable.))]
      (.startup broker)
      {:broker broker})))

(defn stop!
  "Stops a kafka broker.

   Shuts down the broker, releases the latch, and deletes log files"
  [{:keys [broker config log-dirs]}]
  (when broker
    (try
      (.shutdown broker)
      (.awaitShutdown broker)
      {:broker nil}
      (finally
        (fs/try-delete! (io/file log-dirs))))))