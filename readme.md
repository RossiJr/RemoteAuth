[//]: # (<div>)

[//]: # (  <img alt="RemoteAuth Logo" src="https://i.ibb.co/wK0Y58T/Remote-Auth-Logo.png">)

[//]: # (</div>)

# RemoteAuth

## Why to use RemoteAuth?

RemoteAuth is a simple and easy to use library that allows you to authenticate users in your application using a remote
server. This is useful when you have multiple applications, and you want to authenticate users in all of them using a
single server.

## Features

- Authenticate users using a remote server
    - [x] Directly connect to a SQL Database Management System (DBMS)
    - [ ] Directly connect to a NoSQL Database **(NOT IMPLEMENTED YET)**
    - [ ] Use a REST API **(NOT IMPLEMENTED YET)**
- Register users using a remote server
    - [x] Directly connect to a SQL Database Management System (DBMS)
    - [ ] Directly connect to a NoSQL Database **(NOT IMPLEMENTED YET)**
    - [ ] Use a REST API **(NOT IMPLEMENTED YET)**
- Make users to teleport to a spawn point when logging in

### Available DBMSs

- [x] PostgresSQL
- [ ] MySQL **(NOT IMPLEMENTED YET)**
- [ ] SQL Server **(NOT IMPLEMENTED YET)**
- [ ] SQLite **(NOT IMPLEMENTED YET)**
- [ ] Oracle **(NOT IMPLEMENTED YET)**
- [ ] MariaDB **(NOT IMPLEMENTED YET)**
- [ ] MongoDB **(NOT IMPLEMENTED YET)**

## Download, Usage and Examples

### Installation

#### Download

To download RemoteAuth, you can use this [link] in Spigot to download the latest version of the library. After
downloading the jar file, you can add it to your server's `\plugins` folder.

Make sure to configure properly the database connection and the spawn point (optional) before starting the server.

#### Database Configuration

To configure RemoteAuth, you need to create a file in the plugin directory `\plugins\RemoteAuth\db.properties`. This file
should contain the following properties, as shown in the example below:

```properties
# THOSE ARE THE DEFAULT VALUES, YOU MUST CHANGE TO YOUR OWN VALUES #
db.dbms=postgresql
db.host=localhost
db.port=5432
db.database=RemoteAuthTest
db.username=postgres
db.password=postgres
```

- `db.dbms` - The DBMS you are using. Use the one of the following parameters: **(REQUIRED)**
    - PostgresSQL: `postgresql`
- `db.host` - The host of the database. **(REQUIRED)**
- `db.port` - The port used for your database. **(REQUIRED)**
- `db.database` - The name of the database you are using. **(REQUIRED)**
    - Currently, the queries use a default schema and user table `public.user`, the option to change those
      configurations is not available yet, but will be added soon (if you need to use another table reference, you must
      **manually** change the queries and then recompile the plugin).
- `db.username` - The username used to connect to the database. **(REQUIRED)**
- `db.password` - The password used to connect to the database. **(REQUIRED)**

> **If the file doesn't exist or any errors have happened during the connection to the database, the plugin will shut the server down.**

#### Spawn Configuration

If you want to use the spawn point feature, you must configure it with one of the options below, or, in case you don't
want to use it, you can skip this step.

> **If you don't want the player being teleported when logging in, you must make sure the `spawn` object in
the `/plugin/RemoteAuth/config.yml` file is empty.**

##### 1. First option - With command

- The spawn point is used to teleport the player when they log in. If the spawn point is not set, the player won't be
  teleported.
- To configure the spawn point you must use the `/rasetspawn` command
- This command will set the spawn point to the location where the command was executed.
    - set the coordinates `X`, `Y` and `Z`
    - set the direction the player is looking with the `pitch` for the vertical direction and `yaw` for the horizontal
      direction

##### 2. Second option - Manually

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

### Build

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

### Commands

|     Name      |                                Usage                                |                 Command                  |      Permission       |
|:-------------:|:-------------------------------------------------------------------:|:----------------------------------------:|:---------------------:|
|   **Login**   |       Log in the player with the given username and password.       |           `/login <password>`            |           -           |
| **Register**  |            Register the player with the given password.             | `/register <password> <confirmPassword>` |           -           |
| **Set Spawn** | Set the spawn point to the location where the command was executed. |              `/rasetspawn`               | `remoteauth.setspawn` |

## Contact

If you have any questions, suggestions or want to contribute, please contact me
at [my Spigot](https://www.spigotmc.org/members/rossijr.1217740/) or at Discord: `rossijr - RossiJr#4823`.