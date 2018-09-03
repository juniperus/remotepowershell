<#
 # Copyright 2018 Alessio Pinna <alessio@aiselis.com>.
 #
 # Licensed under the Apache License, Version 2.0 (the "License");
 # you may not use this file except in compliance with the License.
 # You may obtain a copy of the License at
 #
 #      http://www.apache.org/licenses/LICENSE-2.0
 #
 # Unless required by applicable law or agreed to in writing, software
 # distributed under the License is distributed on an "AS IS" BASIS,
 # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 # See the License for the specific language governing permissions and
 # limitations under the License.
 #>

Get-ChildItem -Path '{{path}}' -Force | Select-Object -Property Name,
    @{Name='LastWriteTime'; Expression={$_.LastWriteTime.ToString('yyyyMMddhhmmsszzzz')}},
    @{Name='LastAccessTime'; Expression={$_.LastAccessTime.ToString('yyyyMMddhhmmsszzzz')}},
    @{Name='CreationTime'; Expression={$_.CreationTime.ToString('yyyyMMddhhmmsszzzz')}},
    Mode, Length | Format-List;