# Adding 2FA to the user registration flow

This is the first set of changes. You will modify the user registration process so that they are prompted to add this app to an authenticator app on their smartphone.

Feel free to check the git tag [`registration-done`](https://github.com/mjg123/Spring-Boot-2FA/tree/registration-done) if you need a pointer.
  
## Changes to user registration flow

When a user registers, we need to:
  - Be able to use an OTP library
  - Create a secret2FaCode before persisting the UserDto
  - Share the secret2FaCode with the user, in the form of a QR code which can be scanned with an authenticator app

When this is done:
  - users can create accounts as before, and will be shown a QR code during the registration process
  - a secret is stored against each user in the database (different for each user)
  - login is still username/password only.

_Hint:_ look for lines marked with `TODO: (registration)`

## Walkthrough

This is the list of changes we need to make, in a logical order. If you are stuck, click on any of the "Finished Code" links to see how I implemented that part.

1. We need to use an OTP library. I like [JOTP](https://github.com/amdelamar/jotp). Uncomment the dependency in `pom.xml`. ([Finished Code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/pom.xml#L52-L57)).
1. For each user, we need to store a secret key in the database. Modify `data.sql` to add a new column. ([Finished Code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/src/main/resources/data.sql#L7-L12)).
1. The `UserDto` class should match the database schema. Add a new field and getter/setter matching your new DB column. ([Finished Code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/src/main/java/lol/gilliard/springboot2fa/UserDto.java#L25-L28)).

    _Feel free to start the app and inspect the database schema._

1. Generate a secret key (using `OTP.randomBase32(20)`) when a new user registers, and store it in the database. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/src/main/java/lol/gilliard/springboot2fa/UserService.java#L30-L31))
1. Create a QR code for the user's authenticator app (using `OTP.getURL`) in `WebController.java`. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/src/main/java/lol/gilliard/springboot2fa/WebController.java#L79-L87))
1. Show the user the QR code on the "Thank you for registering page", `registered.html`. ([Finished code](https://github.com/mjg123/Spring-Boot-2FA/blob/registration-done/src/main/resources/templates/registered.html#L20-L26)).

That's all the changes we need for the registration flow.

You can now start the app and register for an account. You will be shown a QR code which will add an account to your authenticator app. You can also check the database to see that new users have a 20-character random string in their `secret` row.

Users can still log in using just username and password. Lets fix that. Move on to the next section: [login](login.md).