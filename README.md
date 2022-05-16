# springboot-s3-localstack
Spring boot application integrated with s3 and localstack

1. First one we need run docker-compose to create bucket s3 local.
   1. docker-compose up -d

2. Run application.
   1. maven install
   2. java -jar springboot-s3-localstack

3. Go to /postman and import collection to tests.