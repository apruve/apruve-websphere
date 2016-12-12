# Apruve WebSphere Commerce Plugin

Allows stores built on IBM WebSphere Commerce to integrate Apruve into their checkout.

## Deploy and setup instructions 

1. Add the `ZApruvePaymentPlugin` project to the list of projects you extract.
2. In case your extract script is not pulling all struts config xmls, make sure you add `struts-config-apruve.xml` to the list.
3. Modify Build & Deploy and add `ZApruvePaymentPlugin.jar` to list of EJB modules. This will be one of the projects in your workspace.
4. If your script is not including all acp xmls under `acp/common`, include `acp/common/ApruveACPolicy.xml`.
5. Manually run sqls under `sql/common/Apruve.sql`. 
    > **Important:** Only run this script once!
    
6. Import certificates:
    * Go to the WAS Admin console
    * Expand the Security menu and click on SSL certificate and key management
    * Click Key stores and certificates on the right side of the screen
    * Click NodeDefaultTrustStore
    * Click Signer certificates on the right side of the screen
    * Click the Retrieve from port button at the top of the page
    * Fill in the following info:
        1. **Host:** aprvdev.zobristinc.com(host name of the webhook url given in merchant site)
        2. **Port:** 443
        3. **SSL configuration for outbound connection:** NodeDefaultSSLSettings
        4. **Alias:** aprvdev.zobristinc.com
    * Then click the Retrieve signer information button below the Alias input box
    * Review then click the OK button
    * Click Save at the top the page
    * Restart your app server
