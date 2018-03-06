; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
(ns dda.pallet.dda-git-crate.infra.schema
  (:require
   [clojure.tools.logging :as logging]
   [schema.core :as s]
   [pallet.api :as api]
   [pallet.actions :as actions]
   [pallet.crate :as crate]
   [pallet.crate.git :as git]))
(def ServerTrust
  {(s/optional-key :pin-fqdn-or-ip) s/Str
   (s/optional-key :fingerprint) s/Str})

(def GitRepository
  {:repo s/Str
   :local-dir s/Str})

(def UserGitConfig
  {:email s/Str
   :trust [ServerTrust]
   :repo [GitRepository]})

(def GitConfig
  ;TODO: Docu, Keyword is user-name
  {s/Keyword UserGitConfig})
