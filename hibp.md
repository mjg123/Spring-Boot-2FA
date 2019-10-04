# Extra credit

You could add a call to the [Have I Been Pwned API](https://haveibeenpwned.com/API/v3) to 
discourage users from using common passwords.

*Hint:* I implemented this in the `hibp` tag in the repo.

I've used [Gideon le Grange's HIBP client library](https://github.com/GideonLeGrange/haveibeenpwned), 
and implemented most of the code in the `WebController`.

Code Changes:

 - `WebSecurityConfig` - create a new factory method for creating instances of the
  HIBP client. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/hibp/src/main/java/lol/gilliard/springboot2fa/WebSecurityConfig.java#L67-L72))
 - `WebController` - use the HIBP client in the `registerNewUser` method to reject
  registration if the password is known to have been pwned.
    - [Finished code - new private method](https://github.com/mjg123/Spring-Boot-2FA/blob/hibp/src/main/java/lol/gilliard/springboot2fa/WebController.java#L54-L65)
    - [Finished code - using the method in the controller](https://github.com/mjg123/Spring-Boot-2FA/blob/hibp/src/main/java/lol/gilliard/springboot2fa/WebController.java#L77-L82)