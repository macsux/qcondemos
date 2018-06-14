== Instructions

Config server URLs are made up of 3 levels:

`http://localhost:8888/{APPNAME}/{PROFILENAME}/{LABEL}`


== Exercises

. Access `http://localhost:8888/helloworld/prod`
.. Note how 'helloworld' is mapped to the name of the app, and 'prod' is mapped to spring profile
.. Note how a superset file called application.yml is used to apply values that are commonn to all apps

. Access `http://localhost:8888/helloworld/dev`
.. Note the profile specific property source `helloworld-*dev*.yml` being used because we specified a matching profile. Less specific property sources are also included where `helloworld.yml` applies to all profiles, and `application.yml` applies to all apps.

. Access `http://localhost:8888/helloworld/dev/release`
.. Note how the value in helloworld.yml has changed, because we specified a _label_ called `release`. This mapped to branch with same name in our git repo. In this branch the value inside helloworld.yml is different.
.. Labels can also map to Git tags or commit hashes. Try it now:
... Access `http://localhost:8888/helloworld/dev/release-tag` to read by tag
...  Access `http://localhost:8888/helloworld/dev/9b5c04dcc1355109c9032a33474380eca6501973` to read by hash


== Encryption
Ensure that you have JCE unlimited strength key extensions installed for your JRE. You can download them here: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
Place them inside your %JAVA_HOME%/lib/security

. Set the encryption key inside config server (not your client app)
.. Open src\main\resources\configserver.yml
.. Set the desired symetric encryption key by adding

----
 encrypt:
   key: mysupersecretencryptionpassword
----
. Run config server

----
    mvn spring-boot:run
----
. Create an encrypted value by invoking POST on http://localhost:8888/encrypt. In the body use the text you want to encrypt (eg: passwordtomydatabase). Copy the encrypted value out of the response
. Decrypt the value by sending a POST request to http://localhost:8888/decrypt with encrypt value as body
. Hit http://localhost:8888/helloworld/default/encrypted to access a value in the encrypted branch. 
.. Notice how it returns already decrypted by the config server


== Using Hashicorp Vault as backend storage
. Create an environmental variable to talk to local vault server

    set VAULT_ADDR=http://127.0.0.1:8200

. Launch server from command line via 

    vault server --dev
. Use another console window to load data into vault using the same data as in our git repo:
----
    vault write secret/helloworld DisplayMessage="If at first you don't succeed; call it version 1.0"
    vault write secret/helloworld,dev DisplayMessage="Hey! It compiles! Ship it!"
    vault write secret/application FailureMessage="Failure is not an option - it comes bundled with Windows"
----

. Use `vault token lookup` comamnd to record the ID of the token to talk to Vault

. Modify config server settings to include integrate with vault 
.. Open src\main\resources\configserver.yml
.. Change active profile to be `vault` instead of `git`. 
. Restart config server 

    mvn spring-boot:run
. Use postman requests we used previously to validate that values are coming from Vault. 
.. Under Headers, add new header with key `X-Config-Token` and for value use the ID of token you recorded in previous step
.. Notice that the source of the values are now coming from vault (eg. `"name": "vault:helloworld"`)

. Configure client application by configuring Vault token in bootstrap.yml

    spring:
      cloud:
        config:
          token: YourVaultToken

.. Same key path to configure token can be applied on .NET client by modifying application.json