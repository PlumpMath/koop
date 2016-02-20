(ns koop.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [koop.core-test]))

(enable-console-print!)

(doo-tests 'koop.core-test)
