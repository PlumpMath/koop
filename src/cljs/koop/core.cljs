(ns koop.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]
            [cljs.pprint :as pp]))

(enable-console-print!)

;(load-namespace 'koop.core)
;(in-ns 'koop.core)


(defui MemberView

  ;static om/Ident
  ;(ident [this {:keys [id]}]
  ;  [:id id])

  ;static om/IQueryParams
  ;(params [this]
  ;  {:id 42})

  static om/IQuery
  (query [this]
    [:id :name :address])

  Object
  (render [this]
    (dom/li #js {:className "member"} (:name (om/props this)) )))

(def member-view (om/factory MemberView))

(defui MembersView

  static om/IQuery
  (query [this]
    [{:members (om/get-query MemberView)}])

  Object
  (render [this] (let [{:keys [members]} (om/props this)]
                             (dom/ul #js {:className "members-list"}
                                      (apply dom/div #js {:id "member-wrap"}
                                             (map member-view members))))))


(def members-view (om/factory MembersView))

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
    ;(let [subquery (om/get-query MembersView)]
    ;  `[{:members ~subquery}])                             ;{:list/two ~subquery}
      ;[{:members (om/get-query MemberView)}]
    [(om/get-query MembersView)]
    )

  Object
  (render [this] (let [props (om/props this)]
                   (dom/div #js {:className "main"}
                            (pp/pprint props)
                            (members-view (:members props))
                            ))))

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

  static om/IQuery
  (query [this]
    [(om/get-query MembersView)]
    )

       Object
       (render [this]
               (dom/div #js {:className "root"}
                (header-view)
                (main-view)
                (footer-view))))

(def root-view (om/factory RootView))


;;
;; Data
;;
(defonce app-state (atom
                     {:members  {123 {:id 123 :name "Alan" :address "11916 Big Blue Road"} 456 {:id 456 :name "Dave" :address "123 Abbywood"}}
                      :projects {976 {:id 976 :name "Hermes" :members [123 456]} 654 {:id 654 :name "Clara" :members [123]}}}))

;;
;; Data read multimethods
;;

(defn get-members [state key]
  (let [st @state]
    (pp/pprint key)
    (into [] (map #(get-in st %)) (get st key))))

(defn get-projects [state key]
  (let [st @state]
    (into [] (map #(get-in st %)) (get st key))))

(defmulti read om/dispatch)

(defmethod read :members
  [{:keys [state] :as env} key params]
  {:value (get-members state key)})

(defmethod read :projects
  [{:keys [state] :as env} key params]
  {:value (get-projects state key)})

(def reconciler
  (om/reconciler
    {:state     (atom app-state)
     :normalize false
     ;:read read
     ;:parser    (om/parser {:read read })                   ;:mutate mutate
     ;:send      (util/transit-post "/api")
     }))

(om/add-root! reconciler root-view (gdom/getElement "app"))
