# SPICE-Opensource

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=medtronic-labs_spice-server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=medtronic-labs_spice-server)  [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=medtronic-labs_spice-server&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=medtronic-labs_spice-server)

This is the backend for the Spice app to help track hypertensive and diabetic patients across a population.

## Tech stack

- Git
- Java 17.x
- Apache Maven 3.8.x
- Docker 20.10.xx
- Docker-compose 1.29.x

## Installation

To bring the Spice backend up and running, there are few prerequisite that has to be done.
You can either follow the commands or access the official documentation by clicking the hyperlink.

- To install git in `ubuntu` run the following command or click [Git Official site].

```sh
sudo apt install git
```

- To install git in `windows` visit [Git Official site].


- To install java in `ubuntu` run the following command or click [Java Official site].

```sh
sudo apt install openjdk-17-jdk
```

- To install java in `windows` visit [Java Official site].


- To download and install `maven` in `ubuntu` click [Maven Official site - Download]
  and [Maven Official site - Install].


- After installing the maven zip file open the download folder and extract it.


- To set maven home path in Ubuntu run the following command.

```sh
nano ~/.bashrc
```

- Paste the following lines in .bashrc file.

```Environment variable
MAVEN_HOME='/home/ubuntu/Downloads/apache-maven-3.8.8'
PATH="$MAVEN_HOME/bin:$PATH"
export PATH
```

- Then save the .bashrc file.
- To apply the changes in the .bashrc file run the following command.

```sh
. ~/.bashrc
```

- Note: Once you have executed the script mentioned above, please restart the machine.
- To verify the maven installation, run the following command and check the version

```sh
mvn -v
```

- To download `maven` in `windows` visit [Maven Official site - Download].
- To install `maven` in `windows` visit [Maven Official site - Install].

- To install docker follow steps in the [Docker Official Docs]
- To install docker-compose in `ubuntu` follow steps in the [Docker Compose Official Docs]

## Setup

- Clone the spice-server repository.

```sh
git clone https://github.com/Medtronic-LABS/spice-server.git
```

## Configuration

To run the application, you should pass the necessary configuration via environment properties.
To achieve this, create a ***.env*** file and pass your own values for the following properties.

>**Note:**
Please paste the ***.env*** file inside the specified directory.

`/spice-server/`

***.env*** **file**

```properties
PROJECT_PATH=/home/ubuntu/spice-server
DATABASE_URL=jdbc:postgresql://mypostgres:5432/spice_open_source?serverTimezone=UTC
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
DATABASE_NAME=spice_open_source
NOTIFICATION_PROJECT_PATH=/home/ubuntu/spice-server
REDIS_PASSWORD=password
REDIS_HOST=192.169.33.10
REDIS_PORT=6379
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
IS_PRESCRIPTION_SIGNATURE_UPLOADED_TO_MINIO=false
URL_MINIO=http://127.0.0.1:9000
CONSOLE_URL_MINIO=http://127.0.0.1:9090
ACCESS_NAME_MINIO=minioadmin
ACCESS_SECRET_MINIO=minioadmin
BUCKET_NAME_MINIO=test
ENABLE_SMS_NOTIFICATION_TWILIO=false
TWILIO_ACCOUNT_SID=tyretyey436435
TWILIO_AUTH_TOKEN=gfhfdhdfg64564
TWILIO_FROM_PHONENO=+3535354
SENDGRID_API_KEY=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
ENABLE_SMS_NOTIFICATION_SNS=false
MAIL_FROM=spice@test.com
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_PASSWORD=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
MAIL_USER=apikey
APP_VERSION=1.0.0
EMAIL_APP_URL=http://spicetest.com/reset-password/
#AWS_ACCESS_KEY=
#AWS_SECRET_KEY=

```
>Note: The values for the environmental variables should be changed based on the chosen service.

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

`ENABLE_SMS_NOTIFICATION_TWILIO`: This property indicates whether the SMS to be sent through twilio.

`TWILIO_ACCOUNT_SID`: This property specifies the twilio account sid.

`TWILIO_AUTH_TOKEN`: This property specifies the twilio auth token.

`TWILIO_FROM_PHONENO`: This property specifies the twilio phone number.

`ENABLE_SMS_NOTIFICATION_SNS`: This property indicates whether the SMS to be sent through AWS SNS.

`AWS_ACCESS_KEY`: This property specifies aws access key to send sms.

`AWS_SECRET_KEY`: This property specifies aws secret key to send sms.

`MAIL_FROM`: This property specifies the verified sender's email.

`MAIL_HOST`: This property specifies the mail host.

`MAIL_PORT`: This property specifies the mail port number.

`MAIL_PASSWORD`: This property specifies the mail password.

`MAIL_USER`: This property specifies username.

`SENDGRID_API_KEY`: This property specifies the sendgrid api key that is created.

`LOCAL_FILE_SYSTEM_PATH`: This property specifies the path to the directory on the server where prescription signatures
are stored.

`IS_PRESCRIPTION_SIGNATURE_UPLOADED_TO_S3`: This property indicates whether prescription signatures uploaded to
S3 bucket.

`IS_PRESCRIPTION_SIGNATURE_UPLOADED_TO_MINIO`: This property indicates whether the prescription to be sent through AWS SNS.

`URL_MINIO`: This property specifies the url of the minio service.

`CONSOLE_URL_MINIO` : This property specifies the console url of the minio service.

`ACCESS_NAME_MINIO`: This property specifies the username of the minio service.

`ACCESS_SECRET_MINIO`: This property specifies the password of the minio service.

`BUCKET_NAME_MINIO`: This property specifies the bucket created in the minio service.

`APP_VERSION`: This property specifies the app version of the spice application in the mobile device.

`EMAIL_APP_URL`: This property specifies the app url of the application

## Alternative solution to AWS services using open-source and free service.

-   Storage Service - [MinIO](Open Source)
-   Mail Service - [SendGrid](Free service)
-   SMS Service - [Twilio](Free service)

> Note: When you choose to use the open-source MinIO service for storage.

### MinIO Storage service Setup

- To install [MinIO in Linux] run the following commands or click [Min.io site -Download].
```sh
  wget https://dl.min.io/server/minio/release/linux-amd64/archive/minio_20230916010147.0.0_amd64.deb -O minio.deb
  sudo dpkg -i minio.deb
  mkdir ~/minio
  minio server ~/minio --console-address :9090
```
- To install [MinIO in Windows]
- To install [MinIO in MacOS]

> Note: If you are using the min.io service, please enter the corresponding values for the fields `URL_MINIO`, `CONSOLE_URL_MINIO`, `ACCESS_NAME_MINIO`, `ACCESS_SECRET_MINIO` and `BUCKET_NAME_MINIO` in .env.

### Sendgrid Mail service Setup

> Note: When you choose to use the Sendgrid email free service, follow the below instructions.

- Add the sendgrid dependency in the file: spice-server/backend/notification-service/pom.xml
```sh
        <dependency>
            <groupId>com.sendgrid</groupId>
            <artifactId>sendgrid-java</artifactId>
            <version>4.0.1</version>
        </dependency>
```
- Replace the code in the file: spice-server/backend/notification-service/src/main/java/com/mdtlabs/coreplatform/notificationservice/service/impl/EmailServiceImpl.java from the line 105 to 137.
```sh
         Mail mail = new Mail();
         mail.setSubject(emailDto.getSubject());
         mail.addContent(new Content("text/HTML", emailDto.getBody()));
         mail.setFrom(new Email(mailFrom));
         Personalization personalization = new Personalization();
         personalization.addTo(new Email(StringUtils.isNotBlank(emailDto.getTo()) ? emailDto.getTo() : Constants.EMPTY));
         if (StringUtils.isNotBlank(emailDto.getCc())) {
             personalization.addCc(new Email(emailDto.getCc()));
         }
         if (StringUtils.isNotBlank(emailDto.getBcc())) {
             personalization.addBcc(new Email(emailDto.getBcc()));
         }
         mail.addPersonalization(personalization);
         SendGrid sendGrid = new SendGrid(sendgridApikey);
         Request request = new Request();
         request.setMethod(Method.POST);
         request.setEndpoint("mail/send");
         request.setBody(mail.build());
         sendGrid.api(request);
```
- Visit the [Sendgrid] official site and create a free account to get the sendgrid api key.

> Note: If you are using the sendgrid service, please enter the corresponding values for the fields `MAIL_FROM` and `SENDGRID_API_KEY` in .env.

### Twilio SMS service Setup

> Note: When you choose to use the Twilio SMS free service, follow the below instructions.

- Visit the [Twilio] official site and create a free account to get the account sid, auth token and twilio phone number.

> Note: If you are using the twilio service, please enter the corresponding values for the fields `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN and `TWILIO_FROM_PHONENO` in .env.

## Deployment

Run the following commands in the below path

Build a clean-install using maven
`/spice-server`

```sh
mvn clean install
```

Build docker images by the following command

```sh
docker-compose build
```

Once the images are built, run the docker containers by the following docker command

```sh
docker-compose up
```

## Validation

Once the deployment of the application is successful, you can confirm the connectivity of the Back-end by logging into
the application. To accomplish this, you may choose any API Testing tool. In this particular case, the Postman API
Platform was utilized as an example.

### Endpoint

`POST {{ipAdd}}:8762/auth-service/session`

### Request Body

This endpoint allows user to sign-in into the application.
The request body should be in the **form-data** format and include the following fields:

- `username`: superuser@test.com
- `password`:
  1be35bc75f8316c2b5e48203d93ab3043b4774a7ab4a9e9eebf963c283cde32ddfb95aa843aee3d345f78a551a069200f013cd98904011ea6fb0cd08087d4841

#### The response contains two values: Authorization and TenantId. You must use these values in the subsequent requests.

### Swagger:

Access the Swagger UI documentation in the project using the following URL.

http://localhost:8762/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

> Note:
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

[MinIO in Linux]: <https://min.io/docs/minio/linux/index.html>

[MinIO in Windows]: <https://min.io/docs/minio/windows/index.html>

[MinIO in MacOS]: <https://min.io/docs/minio/macos/index.html>

[MinIO]: <https://min.io/>

[Sendgrid]: <https://sendgrid.com/free/>

[Twilio]: <https://www.twilio.com/try-twilio>

[SPICE DOCUMENTATION](https://spice.docs.medtroniclabs.org/overview/what-is-spice)
