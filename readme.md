<div>
<a href="https://www.spigotmc.org/resources/remoteauth.116205/" target="_blank" alt="Spigot plugin page">
  <img alt="RemoteAuth Logo" height="100" width="500" src="https://i.ibb.co/xjZ3rJx/remoteauth-high-resolution-logo-transparent.png">
</a>
</div>
<p>
<a href="https://discordapp.com/users/261526981701533697" target="_blank" alt="Discord">
<img src= "https://img.shields.io/badge/-Discord-1a1a1a?style=for-the-badge&amp;logo=Discord&amp;logoColor=79ff97&amp;link=https://discordapp.com/users/261526981701533697" style="max-width:100%;" alt="Discord logo"> </a>
<a href="https://www.spigotmc.org/members/rossijr.1217740/" target="_blank" alt="Spigot profile">
<img src= "https://img.shields.io/badge/-Spigot-1a1a1a?style=for-the-badge&amp;logo=SpigotMC&amp;logoColor=79ff97&amp;link=https://www.spigotmc.org/members/rossijr.1217740/" style="max-width:100%;" alt="Spigot logo"> </a>
</p>

## Table of Contents

- [Why to use RemoteAuth?](#why-to-use-remoteauth)
- [Features](#features)
  - [Available DBMSs](#available-dbmss)
- [Download, Usage, and Examples](#download-usage-and-examples)
  - [Installation](#installation)
    - [Download](#download)
    - [Database Configuration](#database-configuration)
    - [Spawn Configuration](#spawn-configuration)
      - [First option - With command (RECOMMENDED)](#db-config-first-option)
      - [Second option - Manually](#db-config-second-option)
    - [Messages Configuration](#messages-configuration)
    - [Build](#build)
    - [Commands](#commands)
  - [Contact](#contact)


## Why to use RemoteAuth? <a name="why-to-use-remoteauth"></a>

RemoteAuth is a simple and easy to use plugin that allows you to authenticate users in your application using a remote
server. This is useful when you have multiple applications, and you want to authenticate users in all of them using a
single server.

## Features <a name="features"></a>

- Authenticate users using a remote server
    - [x] Directly connect to a SQL Database Management System (DBMS)
    - [ ] Directly connect to a NoSQL Database **(NOT IMPLEMENTED YET)**
    - [ ] Use a REST API **(NOT IMPLEMENTED YET)**
- Register users using a remote server
    - [x] Directly connect to a SQL Database Management System (DBMS)
    - [ ] Directly connect to a NoSQL Database **(NOT IMPLEMENTED YET)**
    - [ ] Use a REST API **(NOT IMPLEMENTED YET)**
- Make users to teleport to a spawn point when logging in

### Available DBMSs <a name="available-dbmss"></a>

- [x] PostgresSQL
- [ ] MySQL **(NOT IMPLEMENTED YET)**
- [ ] SQL Server **(NOT IMPLEMENTED YET)**
- [ ] SQLite **(NOT IMPLEMENTED YET)**
- [ ] Oracle **(NOT IMPLEMENTED YET)**
- [ ] MariaDB **(NOT IMPLEMENTED YET)**
- [ ] MongoDB **(NOT IMPLEMENTED YET)**

## Download, Usage and Examples <a name="download-usage-and-examples"></a>

### Installation <a name="installation"></a>

#### Download <a name="download"></a>

To download RemoteAuth, you can use this [link](https://www.spigotmc.org/resources/remoteauth.116205/) in Spigot to download the latest version of the library. After
downloading the jar file, you can add it to your server's `/plugins` folder.

Make sure to configure properly the database connection and the spawn point (optional) before starting the server.

#### Database Configuration <a name="database-configuration"></a>

To configure RemoteAuth, you need to create a file in the plugin directory `/plugins/RemoteAuth/db.properties`. This file
should contain the following properties, as shown in the example below:

```properties
# THOSE ARE THE DEFAULT VALUES, YOU MUST CHANGE TO YOUR OWN VALUES #

# REQUIRED FIELDS
db.dbms=postgresql
db.host=localhost
db.port=5432
db.database=RemoteAuthTest
db.username=postgres
db.password=postgres

# OPTIONAL FIELDS
db.user_schema=public
db.user_table=users
db.uuid_column=uuid
db.username_column=username
db.password_column=password
```

- `db.dbms` - The DBMS you are using. Use the one of the following parameters: **(REQUIRED)**
    - PostgresSQL: `postgresql`
- `db.host` - The host of the database. **(REQUIRED)**
- `db.port` - The port used for your database. **(REQUIRED)**
- `db.database` - The name of the database you are using. **(REQUIRED)**
- `db.username` - The username used to connect to the database. **(REQUIRED)**
- `db.password` - The password used to connect to the database. **(REQUIRED)**
- `db.user_schema` - The schema where the user's table is located. **(OPTIONAL)**
- `db.user_table` - The table where the user's data is stored. **(OPTIONAL)**
- `db.uuid_column` - The column where the user's UUID is stored. **(OPTIONAL)**
- `db.username_column` - The column where the user's username is stored. **(OPTIONAL)**
- `db.password_column` - The column where the user's password is stored. **(OPTIONAL)**

> - **In case of no optional fields set, the plugin will use the default values (the ones shown in the example above).**
> - **If the file doesn't exist or any error has happened during the connection to the database, the plugin will shut the server down.**

#### Spawn Configuration <a name="spawn-configuration"></a>

The spawn point is used to teleport the player when they log in. If the spawn point is not set, the player won't be teleported.

If you want to use the spawn point feature, you must configure it with one of the options below, or, in case you don't
want to use it, you can skip this step.

> **If you don't want the player being teleported when logging in, you must make sure the `spawn` object in
the `/plugin/RemoteAuth/config.yml` file is empty.**

##### 1. First option - With command (**RECOMMENDED**) <a name="db-config-first-option"></a>

- To configure the spawn point you must use the `/rasetspawn` command
- This command will set the spawn point to the location where the command was executed.
    - set the coordinates `X`, `Y` and `Z`
    - set the direction the player is looking with the `pitch` for the vertical direction and `yaw` for the horizontal
      direction

##### 2. Second option - Manually <a name="db-config-second-option"></a>

- You can set the spawn point manually in the `config.yml` file inside `/plugins/RemoteAuth` folder.
- There will be a section called `spawn` where you can set the `X`, `Y`, `Z`, `pitch`, `yaw` and `world` values.
    - To define the spawn point for the first time you must use the following format:
        - ```yaml
          spawn:
            X: 0
            Y: 0
            Z: 0
            pitch: 0.0
            yaw: 0.0
            world: world
      
    - To change it, you can use the same format, changing the values.
  > **Make sure to use the same format as the example above. Otherwise, it will not work!!** 

#### Messages Configuration <a name="messages-configuration"></a>
> Added in the version 1.2.0 of the plugin

You can configure the messages that the plugin will send to the player in the `/plugins/RemoteAuth/messages.properties` file in the following format:

```properties
# Example of the messages.properties file
prefix.chat_prefix=&7[&bRemoteAuth&7] &r

messages.welcome.login=&aHello &a&l%player%&a&r - Nice to see you again!\n&a> Use /login <password> to login!
messages.welcome.register=&aHello %player% - Welcome to the server!\n&a> Use /register <password> <confirmPassword> to register!
messages.welcome.login_success= &aYou have successfully logged in!
```
> _**IMPORTANT** -> The default chat modifier is `§`. However, due to encoding issues, you must use `&` instead of `§` in the messages.properties file._
> 
> Note that those are only a few examples of the messages you can configure. In the table below you can see all the messages that can be configured.

|                 Message Key                  |                                                              Description                                                               |         Possible Variables          |                                                   Default Value                                                    |
|:--------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------:|:------------------------------------------------------------------------------------------------------------------:|
|             `prefix.chat_prefix`             |                                               The prefix that will be shown in the chat                                                |                  -                  |                                              `&7[&bRemoteAuth&7] &r`                                               |
|           `messages.welcome.login`           |                       The message that will be shown to the player when they enter in the server - Before log in                       |     `%player%`, `%chatPrefix%`      |                                  `%chatPrefix%§aTo log in, use /login <password>`                                  |
|         `messages.welcome.register`          |                      The message that will be shown to the player when they enter in the server - Before register                      |     `%player%`, `%chatPrefix%`      |       `%chatPrefix%§a§aYou are not registered!\n> §aUse /register <password> <confirmPassword> to register.`       |
|       `messages.welcome.login_success`       |                               The message that will be shown to the player when they log in successfully                               |           `%chatPrefix%`            |                                  `%chatPrefix%§aYou have successfully logged in!`                                  |
|     `messages.welcome.register_success`      |                              The message that will be shown to the player when they register successfully                              |           `%chatPrefix%`            |                               `%chatPrefix%§aYou have been successfully registered!`                               |
|         `messages.success.spawn_set`         |                         The message that will be shown to the player when the spawn point is set successfully                          | `%chatPrefix%`, `%x%`, `%y%`, `%z%` |                              `%chatPrefix%§aSpawn set at (§f%x%§a, §f%y%§a, §f%z%§a)`                              |
|        `messages.error.no_permission`        |                      The message that will be shown to the player when they don't have permission to do something                      |           `%chatPrefix%`            |                               `%chatPrefix%§cYou don't have permission to do that!`                                |
|      `messages.error.already_logged_in`      |                  The message that will be shown to the player when they are already logged in and try to log in again                  |           `%chatPrefix%`            |                                     `%chatPrefix%§cYou are already logged in!`                                     |
|        `messages.error.not_logged_in`        |                 The message that will be shown to the player when they are not logged in and try to do something else                  |           `%chatPrefix%`            |                                       `%chatPrefix%§cYou are not logged in!`                                       |
|  `messages.error.invalid_username_password`  |               The message that will be shown to the player when they try to log in with an invalid username or password                |           `%chatPrefix%`            |                                   `%chatPrefix%§cInvalid username or password!`                                    |
|      `messages.error.command_not_found`      |                     The message that will be shown to the player when they try to use a command that doesn't exist                     |           `%chatPrefix%`            |                                         `%chatPrefix%§cCommand not found!`                                         |
|        `messages.error.server_not_up`        |      The message that will be shown to the player when the server is not up and the player tries to log in or perform any action       |           `%chatPrefix%`            |                               `%chatPrefix%§cThe server is not up, try again later.`                               |
|    `messages.error.critical_login_error`     |       The message that will be shown to the player when a critical error happens during the login process (e.g. database error)        |           `%chatPrefix%`            |           `%chatPrefix%§c§lA critical error occurred while logging in. Please contact an administrator.`           |
|   `messages.error.critical_register_error`   |      The message that will be shown to the player when a critical error happens during the register process (e.g. database error)      |           `%chatPrefix%`            |          `%chatPrefix%§c§lA critical error occurred while registering. Please contact an administrator.`           |
| `messages.error.critical_isregistered_error` | The message that will be shown to the player when a critical error occurs during the checking user process (if it's registered or not) |           `%chatPrefix%`            | `%chatPrefix%§c§lA critical error occurred while checking if you are registered. Please contact an administrator.` |
|   `messages.error.passwords_do_not_match`    |              The message that will be shown to the player when the passwords don't match during the registration process               |           `%chatPrefix%`            |                                      `%chatPrefix%§cPasswords do not match!`                                       |
|         `messages.error.login_usage`         |                    The message that will be shown to the player when they use the `/login` command in the wrong way                    |           `%chatPrefix%`            |                                      `%chatPrefix%§cUsage: /login <password>`                                      |
|       `messages.error.register_usage`        |                  The message that will be shown to the player when they use the `/register` command in the wrong way                   |           `%chatPrefix%`            |                           `%chatPrefix%§cUsage: /register <password> <confirmPassword>`                            |



### Build <a name="build"></a>

> Note that you have to have Maven and Git installed on your machine to build the project in the shown way.

To edit the source code, you can clone this repository and open it in your favorite IDE. After making the changes, you
can build the project using Maven. To do so, you can use the following commands:

```shell
# Navigate to the directory where you want to clone the repository
cd {directory}
```

```shell
# Clone the repository
git clone https://github.com/RossiJr/RemoteAuth.git
```

```shell
# Navigate to the project root folder
cd RemoteAuth
```

```shell
# Build the project | clean: remove any file into target folder
#                   | install: compile the project and install it in the local repository
mvn clean install
```

After this process, you will have the jar file in the `target` folder. You can use this jar file to replace the one in
your server's `\plugins` folder.

### Commands <a name="commands"></a>

|     Name      |                                Usage                                |                 Command                  |      Permission       |
|:-------------:|:-------------------------------------------------------------------:|:----------------------------------------:|:---------------------:|
|   **Login**   |       Log in the player with the given username and password.       |           `/login <password>`            |           -           |
| **Register**  |            Register the player with the given password.             | `/register <password> <confirmPassword>` |           -           |
| **Set Spawn** | Set the spawn point to the location where the command was executed. |              `/rasetspawn`               | `remoteauth.setspawn` |

## Contact <a name="contact"></a>

If you have any questions, suggestions or want to contribute, please contact me
at [my Spigot](https://www.spigotmc.org/members/rossijr.1217740/) or at Discord: `rossijr - RossiJr#4823`.