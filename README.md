# 2FA2Furious Workshop

This repo contains the code for the 2FA2Furious Java Workshop.

The goal of this workshop is to take an application which only has username/password auth and improve security by adding TOTP 2FA to the user registration and login flows.

You will need:

  - Java version 8 or newer (tested up to Java 15)
  - a Java IDE (tested with IntelliJ)
  - git

## Where to start?

There are git tags for each stage of the work. To start, clone the repo at the [`start-here`](https://github.com/mjg123/Spring-Boot-2FA/tree/start-here) tag:

```
git clone https://github.com/mjg123/Spring-Boot-2FA.git -b start-here
```

Your mission is to update this app so that users are prompted to add an account to their authenticator app (eg Authy, Google Authenticator) on registration, and will need a code from that app on login.

Sounds complicated?  Don't worry, there is plenty of help available. In total, you will need to edit 7 files and add about 100 lines of code, all of which is given in the walkthrough if you need it.

All the extension points you need are in the code already - you do not need to create any new classes.

## How to run the app?

Either:

 - _From the command-line:_ `./mvnw clean spring-boot:run`
 - _From your IDE:_ run the main method in the `SpringBoot2faApplication` class.

Then open a browser at [http://localhost:8080](http://localhost:8080)

The app is backed by an in-memory database. You can inspect the database any time you need to at [http://localhost:8080/h2-console](http://localhost:8080/h2-console). DB credentials are [here](https://github.com/mjg123/Spring-Boot-2FA/blob/start-here/src/main/resources/application.properties#L3-L4). The database is recreated from scratch every time you start the app, by the code in `src/main/resources/data.sql`.

## How to proceed?

You should work in the following order:

 - [Add 2FA setup to the user registration flow](registration.md)
 - [Add 2FA requirement to the user login flow](login.md)
 
There's an optional extra-credit section for warning users who have chosen bad passwords:

 - [Add calls to the ;--haveibeenpwned API](hibp.md)


## Code walkthrough

Here are some important classes:

### WebController
`WebController` maps URLs to actions in the app. We have:
 - `/`. Shows a home page, `home.html`. Content is different depending on whether or not you are logged in.
 - `/login`. Shows the login page, `login.html`
 - `/user/registration`:
   - `GET` requests - creates a new (blank) user object shows the registration form, `registration.html`, which POSTs back to the same URL.
   - `POST` requests - attempts to register the new user. If this fails (duplicate username or no password) show the reg form again. If it succeeds, redirect to the "thank you for registering" page.
 - `/user/registered`. "Thank you for registering", and a link to the login page.
 - `/user/home`. Only visible when logged in. If not, you will be redirected to `/login`

All parameters to the methods backing these URLs are created by Spring. Where there is a parameter of type `Principal` it will represent the logged-in user (which might be null).

### WebSecurityConfig
`WebSecurityConfig` defines which URLs are visible when not logged in, and configures a login/logout flow.

This class also defines a couple of security-related beans which Spring needs - a `PasswordEncoder` and an `AuthenticationProvider`.

### UserDto
`UserDto` encapsulates everything we need to know about a user to create their record in the database. Currently (hint), this is just "username" and "password".

### UserService
`UserService` wraps the `UserRepository`, exposing higher-level operations and throwing application-level exceptions when needed.

### CustomAuthenticationProvider
`CustomAuthenticationProvider` extends `DaoAuthenticationProvider` which can perform authentication using a `UserService`.
This class doesn't provide any additional functionality over its superclass, but might (hint) be a useful extension point.
