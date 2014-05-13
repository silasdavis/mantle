##Mantle
Things not in clojure.core, but are general enough that they could be.

###Usage
Add leiningen dependency to project.clj:

```clojure
[silasdavis/mantle "0.1.0"]
```

###Synopsis
```clojure
(use '[mantle.core])

(update-all-in {:a [{:b {:c 2}} {:b {:c 3}}]} [:a [:b :c]] inc)
=> {:a ({:b {:c 3}} {:b {:c 4}})}

(update-all-in nil [:a [:a [:b]]] (constantly 1))
=> {:a ({:a ({:b 1})})}

(update-all-in {:a [{:a [{:b {}}]} 4]} [:a [:a [:b :c]]] (constantly 1))
=> {:a ({:a ({:b {:c 1}})} 4)}
```
