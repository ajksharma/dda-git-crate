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

(ns dda.pallet.dda-git-crate.domain
  (:require
   [schema.core :as s]
   [dda.config.commons.map-utils :as map-utils]
   [dda.config.commons.user-home :as user-home]
   [dda.pallet.commons.secret :as secret]
   [dda.pallet.dda-git-crate.infra :as infra]
   [dda.pallet.dda-git-crate.domain.repo :as repo]))

(def GitConfig
  {:user-email s/Str
   (s/optional-key :signing-key) s/Str
   (s/optional-key :diff-tool) s/Str
   (s/optional-key :credential) repo/GitCredentials
   (s/optional-key :repo) {s/Keyword [repo/GitRepository]}
   (s/optional-key :synced-repo) {s/Keyword [repo/GitRepository]}})

(def GitDomainConfig
  {s/Keyword                 ;represents the user-name
   GitConfig})

(def InfraResult {infra/facility infra/GitConfig})

(defn-
  configuration
  [user-config]
  (let [{:keys [user-email signing-key diff-tool]} user-config]
    (merge
      {:email user-email}
      (when (contains? user-config :signing-key)
        {:signing-key signing-key})
      (when (contains? user-config :diff-tool)
        {:diff-tool diff-tool}))))

(defn-
  infra-configuration-per-user
  [user-config]
  (let [{:keys [user-email signing-key diff-tool credential
                repo synced-repo]} user-config]
    {:config (configuration user-config)
     :trust (repo/trust repo synced-repo)
     :repo []}))

(s/defn ^:always-validate
  infra-configuration :- InfraResult
  [domain-config :- GitDomainConfig]
  {infra/facility
    (into {}
      (map
        (fn [[k v]] [k (infra-configuration-per-user v)])
        domain-config))})
