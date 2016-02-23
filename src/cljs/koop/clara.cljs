(ns koop.clara
  (:require-macros [clara.macros :refer [defrule defquery defsession]])
  (:require [cljs.pprint :as pp]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]
            [clara.rules :refer [insert retract fire-rules query insert! retract!]]))

(enable-console-print!)

(defrecord Member [name address])

(defquery get-members
          "get a list of members"
          []
          [?member <- Member])

(defsession app-session 'koop.clara)
