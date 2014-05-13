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