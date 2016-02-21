(ns koop.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]))

(enable-console-print!)

(defonce app-state (atom {:name "Hello Chestnut!" :address "blah"}))

(defui MemberView

       static om/IQuery
       (query [this]
              [:name :address])

       Object
       (render [this] (dom/div #js {:className "member"} "Member: " )))

(def member-view (om/factory MemberView))


(defui HeaderView
       static om/IQuery
       (query [this]
              [:name :token])

       Object
       (render [this]
               (dom/header #js {:className "header"} "Header View")))

(def header-view (om/factory HeaderView))


(defui MainView
  static om/IQuery
  (query [this]
    [:name :token])

  Object
  (render [this]
    (dom/div #js {:className "main"}
                (member-view)
                )))

(def main-view (om/factory MainView))


(defui FooterView
       static om/IQuery
       (query [this]
              [:name :token])

       Object
       (render [this]
               (dom/footer #js {:className "footer"} "Footer View")))

(def footer-view (om/factory FooterView))


(defui RootView

       Object
       (render [this]
               (dom/div #js {:className "root"}
                (header-view)
                (main-view)
                (footer-view))))

(def root-view (om/factory RootView))

(def reconciler
  (om/reconciler
    {:state     (atom app-state)
     :normalize false
     ;:parser    (om/parser {:read p/read :mutate p/mutate})
     ;:send      (util/transit-post "/api")
     }))

(om/add-root! reconciler root-view (gdom/getElement "app"))
