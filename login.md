# Adding 2FA to the login flow

This is the second set of changes - make sure you have completed the [registration](registration.md) section before you do this.
 
You will modify the user login flow so that they need to use a code from their authenticator app to be able to log in.

Feel free to check the git tag [`login-done`](https://github.com/mjg123/Spring-Boot-2FA/tree/login-done) if you need a pointer.

## Changes to the login flow

When a user logs in, we need to:
  - Grab the 2FA code they provided in the login form
  - Retrieve their UserDto from the database, which will have their 2FA secret as a property
  - Generate a server-side 2FA code based on their 2FA secret from the UserDto and the current time
  - Check that the 2FA code they provided is the same as the server has generated

When this is done:
  - Users will need to provide a TOTP code from their authenticator app in order to log in.
  - Now the app is using TOTP-based 2FA

*Hint:* look for lines marked with `TODO: (login)`

## Walkthrough

This is the list of changes we need to make, in a logical order. If you are stuck, click on any of the "Finished Code" links to see how I implemented that part.

1. Add a field to the login page `login.html` for the user to provide the code from their app. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/login-done/src/main/resources/templates/login.html#L29-L31))
1. Get the user-provided 2FA code from the `HttpServletRequest` in the `CustomAuthenticationDetails` class. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/login-done/src/main/java/lol/gilliard/springboot2fa/auth/custom/CustomAuthenticationDetails.java#L13-L18) - note there's a new field and getter too).
1. In the `CustomAuthenticationProvider` class, generate a 2FA code based on the user's secret on the server using `OTP.create`. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/login-done/src/main/java/lol/gilliard/springboot2fa/auth/custom/CustomAuthenticationProvider.java#L43-L48))
1. Check that the code provided by the user matches the code we generated on the server. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/login-done/src/main/java/lol/gilliard/springboot2fa/auth/custom/CustomAuthenticationProvider.java#L43-L63))

That's all we need!

Start the app and register for a new account. Add the account to your authenticator app (deleting old accounts from this app if necessary). You will need to use the 2FA code from your app to log in.

🎉🎉 **CONGRATULATIONS** 🎉🎉

## Extra credit?

You've added 2FA to this app. Your users' data is better protected, so congrats.

What about calling the [;--haveibeenpwned API](https://haveibeenpwned.com/api/v3) to warn users who try to register with passwords that might have been compromised? [Lets see how to do that](hibp.md). 