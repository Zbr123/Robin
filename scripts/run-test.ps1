param(
    [ValidateSet('registration', 'audit', 'billing', 'login')]
    [string]$Suite = 'registration',
    [string]$Tags = '',
    [switch]$Headed
)

$headedFlag = if ($Headed) { '-Dheaded=true' } else { '' }
$testClass = switch ($Suite) {
    'registration' { 'PatientRegistrationTestRunner' }
    'audit'        { 'AuditTestRunner' }
    'billing'      { 'BillingTestRunner' }
    'login'        { 'TestRunner' }
}

$args = @('test', "-Dtest=$testClass")
if ($headedFlag) { $args += $headedFlag }
if ($Tags) { $args += "-Dcucumber.filter.tags=$Tags" }

Push-Location (Split-Path $PSScriptRoot -Parent)
Write-Host "mvn $($args -join ' ')" -ForegroundColor Cyan
& mvn @args
Pop-Location
