(ns mantle.core)

(defn update-all-in
  "Takes a map or a sequence m and applies f at all locations specified by the key sequence [k & ks], where the
  key sequence may contain nested sub-sequences.

  If the key k is sequential, update-all-in expects that the corresponding node is also sequential and maps
  each element at that node with update-all-in, passing the sub-sequence k as the the key sequence argument.

  So it works like update-in, only it acts on all addresses reachable by mapping over sequential nodes
  when specified by each level of nesting in the key sequence.

  Ignores addresses that would pass through existing non-sequential non-associative values, otherwise
  creates those addresses.

  For example:
    (update-all-in {:a [{:b {:c 2}} {:b {:c 3}}]} [:a [:b :c]] inc)
    => {:a ({:b {:c 3}} {:b {:c 4}})}

    (update-all-in nil [:a [:a [:b]]] (constantly 1))
    => {:a ({:a ({:b 1})})}

    (update-all-in {:a [{:a [{:b {}}]} 4]} [:a [:a [:b :c]]] (constantly 1))
    => {:a ({:a ({:b {:c 1}})} 4)}
  "
  [m [k & ks] f & args]
  (if (sequential? k)
    (map (fn [v] (apply update-all-in v k f args)) (or m [{}]))
    (if (or (nil? m) (associative? m))
      (if ks
        (assoc m k (apply update-all-in (get m k) ks f args))
        (assoc m k (apply f (get m k) args)))
      m)))

(defn singleton-or-coll
  "Converts a single element to a singleton collection or returns the collection itself"
  [x]
  (if (sequential? x) x (if x [x] [])))

(defn singleton-or-set
  "Converts a single element to a singleton set or a sequence of elements into a set"
  [x]
  (into #{} (singleton-or-coll x)))

(defn deep-vals
  "Flattens out the values i levels deep into an associative structure."
  ([kvs] (deep-vals kvs 1))
  ([kvs i] (if (> i 1) (recur (apply concat (map last kvs)) (dec i)) (map last kvs))))

(defn map-map
  "Maps the key-value pairs of coll to other key-value pairs, and returns them as a hash-map."
  [f coll]
  (into {} (map f coll)))

(defn map-keys
  "Maps the keys of coll with f, and returns a map."
  [f coll]
  (map-map (fn [[k v]] [(f k) v]) coll))

(defn map-vals
  "Maps the values of coll with f, and returns a map."
  [f coll]
  (map-map (fn [[k v]] [k (f v)]) coll))

(defn rotate
  "Rotates a list by moving the first element to the end of the list."
  ([coll] (lazy-cat (rest coll) [(first coll)]))
  ([n coll] (lazy-cat (drop n coll) (take n coll))))

(defn fpow 
  "Composes the function f with itself n times and applies it to x."
  [f n x]
  (reduce (fn [y f] (f y)) x (repeat n f)))

(defn split-by*
  [pred coll taken]
  (lazy-seq
    (when-let [s (seq coll)]
      (if (pred (first s))
        (split-by* pred (rest s) (concat taken [(first s)]))
        [taken s]
        ))))


(defn split-by
  "Single pass alternative to split-with."
  [pred coll]
  )

