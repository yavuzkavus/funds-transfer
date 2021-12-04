## Funds Transfer
A RESTful API that performs funds transfer between two accounts with currency exchange.

### Implementation details:
* Implementation has been done in Java/Spring
* No security layer (authentication / authorization) is provided
* Implementation supports being invoked concurrently by multiple users/systems
* The minimal attributes of an Account are:
    * An owner ID (numeric)
    * A Currently (String)
    * A balance (numeric)
* Exchange rates are created on the fly, but can be retrieved from external APIs
* Program runs on an embedded Tomcat
* Functionality covered with tests
* Fund transfer fails if:
    * Either the debit or the credit account does not exist
    * The exchange rate cannot be retrieved
    * The balance of the debit account is not sufficient
