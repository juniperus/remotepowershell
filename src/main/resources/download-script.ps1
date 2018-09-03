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

$currPath = $ExecutionContext.SessionState.Path;
$path = $currPath.GetUnresolvedProviderPathFromPSPath("{{path}}");
If (Test-Path $path -PathType Leaf) {
    $bytes = [System.convert]::ToBase64String([System.IO.File]::ReadAllBytes($path));
    Write-Host $bytes;
    exit 0;
}
ElseIf (Test-Path $path -PathType Container) {
    exit 0;
}
exit 1;