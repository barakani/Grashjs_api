# Atlas CMMS API

This project aims to help manage assets, schedule maintenance and track work orders. This is the REST backend (Java8-Spring Boot) of the web
application developed with React. The frontend developed with React can be
found [here](https://github.com/Grashjs/frontend). We also have
a [React Native mobile application](https://github.com/Grashjs/mobile). The link to the live website can be
found [here](https://atlascmms.com).  
Here are the [current features](Current%20features.pdf). We would be very happy to have new contributors join us.
**And please star the repo**.

**Screenshot**:
![](https://i.ibb.co/7tGYCtv/Screenshot-502.png)

## How to run locally ?

You should first install and use JDK 8 then create a Postgres database. After that go to [src/main/resources/application-dev.yml](src/main/resources/application-dev.yml), change the url, username and password. And set these environment variables.

- `SMTP_USER` after [creating an app password with Google](https://support.google.com/accounts/answer/185833?hl=en)
- `SMTP_PWD` after [creating an app password with Google](https://support.google.com/accounts/answer/185833?hl=en)
- `GCP_JSON` after creating a Google Cloud Storage bucket, then creating a service account following the section **Create a service account** of
  this [tutorial](https://medium.com/@raviyasas/spring-boot-file-upload-with-google-cloud-storage-5445ed91f5bc)
- `GCP_PROJECT_ID` the Google Cloud project id can also be found in the `GCP_JSON`
- `FASTSPRING_PWD` (optional: can replace with a placeholder) after creating a FastSpring account
- `FASTSPRING_USER` optional after creating a FastSpring account
- `MAIL_RECIPIENTS` emails separated by a comma: you can provide your email
- `GCP_BUCKET_NAME` Google Cloud Storage bucket name

Very important run with dev profile. Check [this](https://stackoverflow.com/a/44374099) if you don't know how

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker or send an
email at ibracool99@gmail.com.

## Getting involved

You can contribute in different ways. Sending feedback on features, fixing certain bugs, implementing new features, etc.
Here is the [trello dashboard](https://trello.com/invite/b/dHcnX2Y0/ATTI9f361dff4298643df8ef3a80a1413c42E4308099/grash).
Instructions on _how_ to contribute can be found in [CONTRIBUTING](CONTRIBUTING.md).

## Star History

<picture>
  <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=grashjs/api&type=Date" />
  <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=grashjs/api&type=Date" />
  <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=grashjs/api&type=Date" />
</picture>
----

## Open source licensing info

1[LICENSE](LICENSE)
