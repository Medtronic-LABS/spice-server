# SPICE-Opensource

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=medtronic-labs_spice-server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=medtronic-labs_spice-server)  [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=medtronic-labs_spice-server&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=medtronic-labs_spice-server)  

This is the backend for the Spice app to help track hypertensive and diabetic patients across a population.

## Tech stack

- Git
- Java 17.x
- Apache Maven 3.8.x
- Docker 20.10.xx
- docker-compose 1.29.x

## Installation

To bring the Spice backend up and running, there are few prerequisite that has to be done.
You can either follow the commands or access the official documentation by clicking the hyperlink.

- To install git in `ubuntu` run the following command or click [Git Official site].

```sh
$ sudo apt install git
```

- To install git in `windows` visit [Git Official site].


- To install java in `ubuntu` run the following command or click [Java Official site].

```sh
$ sudo apt install openjdk-17-jdk
```

- To set java home path in `ubuntu` run the following command.

```sh
$ nano ~/.bashrc
```

- Paste the following lines at the end of bashrc file.

```Environment variable
# SET JAVA_HOME PATH
JAVA_HOME=jdk-install-dir
export JAVA_HOME
PATH=$JAVA_HOME/bin:$PATH
export PATH
```

> Note: Once you have executed the script mentioned above, please restart the machine.

- Change the above JAVA_HOME jdk directory to installed jdk directory path.
- To find the jdk-install-directory in `ubuntu` follow the below steps
    * find /usr/lib/jvm/java-17-openjdk/bin
    * copy the exact folder path and paste it in JAVA_HOME
    * like JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    * then save the bashrc file

- To apply the changes in the bashrc file run the following command

```sh
$ . ~/.bashrc
```

- To verify the JAVA_HOME path is set or not run the following command.

```sh
$ echo $JAVA_HOME
```

- To install java in `windows` visit [Java Official site].  


- To download and install `maven` in `ubuntu` click [Maven Official site - Download]
  and [Maven Official site - Install].   


- After installing the maven 3.8.7 zip file open the download folder and extract it.   


- To move the extracted maven directory to opt, open terminal from the download path
- and run the following command

```sh
$ sudo mv apache-maven-3.8.7 /opt/
```

- After moving to /opt/ give the permission for the current user to execute maven directory.
- run the following command

```sh
$ sudo chown -R $USER:$USER /opt/apache-maven-3.8.7
```

- To set maven home path in `ubuntu` run the following command.

```sh
$ nano ~/.bashrc
```

- Paste the following lines at JAVA_HOME and also modify the JAVA_HOME.

```Environment variable
MAVEN_HOME=/opt/apache-maven-3.8.7
export MAVEN_HOME
PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:PATH
export PATH
```

- Then save and apply the changes we have done before by following the
- above JAVA_HOME command.

- To download `maven` in `windows` visit [Maven Official site - Download].
- To install `maven` in `windows` visit [Maven Official site - Install].

- To install docker follow steps in the [Docker Official Docs]
- To install docker-compose in `ubuntu` follow steps in the [Docker Compose Official Docs]

## Setup

- Clone the spice-opensource repository.

```sh
$ git clone https://github.com/Medtronic-LABS/spice-server.git
```

## Configuration

To run the application, you should pass the necessary configuration via environment properties.
To achieve this, create a ***.env*** file and pass your own values for the following properties.

**Note:**
Please paste the ***.env*** file inside the specified directory.

`/spice-opensource/backend/`

***.env*** **file**

```properties
PROJECT_PATH=/home/ubuntu/spice-opensource/backend
DATABASE_URL=jdbc:postgresql://mypostgres:5432/spice_open_source?serverTimezone=UTC
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
DATABASE_NAME=spice_open_source
NOTIFICATION_PROJECT_PATH=/home/ubuntu/spice-opensource/backend
REDIS_PASSWORD=password
REDIS_HOST=192.169.33.10
REDIS_PORT=6379
HERE_MAP_API_KEY=sAmpleKeYSTexT
IS_CLOUD=false
API_KEY_PATH=Dev/HERE_MAP
SERVER_REGION=us-east-1
MAIL_SEND=false
ENABLE_SMS_NOTIFICATION=false
SHEDLOCK_EMAIL_START=PT30S
SHEDLOCK_EMAIL_STOP=PT02M
IS_NEXT_MEDICAL_REVIEW_JOB=false
IS_NEXT_BP_JOB=false
IS_NEXT_BG_JOB=false
SHEDLOCK_OUTBOUND_START=PT24H
SHEDLOCK_OUTBOUND_STOP=PT25H
SHEDLOCK_SMS_START=PT30S
SHEDLOCK_SMS_STOP=PT02M
SWAGGER_ENABLED=true
LOCAL_FILE_SYSTEM_PATH=/Prescription_Signatures
IS_PRESCRIPTION_SIGNATURE_UPLOADED_TO_S3=false
```

## .env

The `.env` file is used to store environment variables for the project. These variables are used to configure the
application and contain sensitive information such as passwords, API keys, and other credentials.

Please note that the `.env` file should never be committed to version control, as it contains sensitive information that
should not be shared publicly. Instead, you should add the `.env` file to your .gitignore file to ensure that it is not
accidentally committed.

To use the application, you will need to create a `.env` file in the root directory of the project and add the necessary
environment variables. You can refer to the above file for an example of the required variables and their format.

***The values provided in the
instructions are for demonstration purposes only and will not work as-is. You will need to replace them with actual
values that are specific to your environment.***

> Note: After checking out the project, please ensure that you update the relevant properties and values in env.example, and then rename it to .env.

## .env description

`PROJECT_PATH`: This property specifies the file path to the project's backend directory in the server.

`DATABASE_URL`: This property contains the JDBC connection URL for the PostgreSQL database server with the server
timezone set to UTC, with the database name.

`DATABASE_USERNAME`: This property contains the username used to connect to the PostgreSQL database server.

`DATABASE_PASSWORD`: This property contains the password to connect to the PostgreSQL database server.

`DATABASE_NAME`: This property contains the name of the PostgreSQL database used by the project.

`NOTIFICATION_PROJECT_PATH`: This property specifies the file path to the notification project.

`REDIS_PASSWORD`: This property contains the password used to authenticate with the Redis server.

`REDIS_HOST`: This property contains the IP address of the Redis server.

`REDIS_PORT`: This property contains the port number used by the Redis server.

`HERE_MAP_API_KEY`: This property contains the API key required to access the HERE Maps API.

`IS_CLOUD`: This property indicates whether the project should retrieve the API Key from the cloud to use it with the
HERE Maps API.

`API_KEY_PATH`: This property specifies the path to the API key required to access the HERE Maps API.

`SERVER_REGION`: This property specifies the region where the Amazon S3 buckets should create.

`MAIL_SEND`: The Application uses this property to indicate whether it sends emails.

`ENABLE_SMS_NOTIFICATION`: This property indicates whether SMS notifications are enabled in the Application.

`SHEDLOCK_EMAIL_START`: This property specifies the start time for the email shedlock job.

`SHEDLOCK_EMAIL_STOP`: This property specifies the stop time for the email shedlock job.

`IS_NEXT_MEDICAL_REVIEW_JOB`: This property indicates whether the next medical review job is enabled.

`IS_NEXT_BP_JOB`: This property indicates whether the next blood pressure job is enabled.

`IS_NEXT_BG_JOB`: This property indicates whether the next blood glucose job is enabled.

`SHEDLOCK_OUTBOUND_START`: This property specifies the start time for the outbound shedlock job.

`SHEDLOCK_OUTBOUND_STOP`: This property specifies the stop time for the outbound shedlock job.

`SHEDLOCK_SMS_START`: This property specifies the start time for the SMS shedlock job.

`SHEDLOCK_SMS_STOP`: This property specifies the stop time for the SMS shedlock job.

`SWAGGER_ENABLED`: This property indicates whether Swagger UI is enabled in the project.

`LOCAL_FILE_SYSTEM_PATH`: This property specifies the path to the directory on the server where prescription signatures
are stored.

`IS_PRESCRIPTION_SIGNATURE_UPLOADED_TO_S3`: This property indicates whether prescription signatures uploaded to
S3 bucket.

## Deployment

run the following commands in the below path

Build a clean-install using maven
`/Opensource/Codebase/spice-opensource/backend`

```sh
$ mvn clean install
```

Build docker images by the following command

```sh
$ docker-compose build
```

Once the images are built, run the docker containers by the following docker command

```sh
$ docker-compose up
```

## Validation

Once the deployment of the application is successful, you can confirm the connectivity of the Back-end by logging into
the application. To accomplish this, you may choose any API Testing tool. In this particular case, the Postman API
Platform was utilized as an example.

### Endpoint

`POST {{ipAdd}}:8762/auth-service/session`

### Request Body

This endpoint allows user to sign-in into the application.
The request body should be in the form-data format and include the following fields:

- `username`: superuser@test.com
- `password`:
  3901e08e724bb73a72137e03e2f03d54d70eaeea39f8a7e0459f15d9e80585ffd2159283b8324d72f7ba8c2aa2f0e82e5c1b685d7ef569e2ebad02d71ba4076e

#### The response contains two values: Authorization and TenantId. You must use these values in the subsequent requests.

### Note:

The credentials displayed in the tables are for demonstration purposes only and should not be
used in a production environment. If you need to remove, modify, or add new user credentials,
you can create a new script file containing the necessary DDL or DML queries in the below specified
location. It's important to note that attempting to update the existing script file may result in
a checksum error.

`user-service/src/main/resources/db/migration`


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[Git Official site]:<https://git-scm.com/book/en/v2/Getting-Started-Installing-Git>

[Java Official site]:<https://www.oracle.com/in/java/technologies/downloads/#java17>

[Maven Official site - Download]: <https://maven.apache.org/download.cgi>

[Maven Official site - Install]:<https://maven.apache.org/install.html>

[Redis Official Docs]:<https://redis.io/docs/getting-started/installation/>

[Docker Official Docs]: <https://docs.docker.com/engine/install/>

[Docker Compose Official Docs]: <https://docs.docker.com/compose/install/linux/>

[Maven Official site -Download]: <https://maven.apache.org/download.cgi>

[SPICE DOCUMENTATION](https://app.gitbook.com/o/RnePNEThd1XTpW5Hf3HB/s/7inBQ0zjo0nwpqK5625P/~/changes/6/overview/installation-document/backend)
