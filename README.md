# SalesManager

This is a simple application includes the basic operations for selling products.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:


```

1- mvn spring-boot: run
or
2- ./mvnw

```

## Creating an account then do a simple scenario 
By default we've disabled security to facilitate the tests, but you can just make the package "/api/*" required the authentication to see the security also...

1-If you want to see user managements and security layer, you should register a new account using the registeration api:

    http://localhost:8080/api/register
    
    But before that you should add some authorities(ADMIN,USER), so it is better to get them as a boiler plate, by importing the csv files in the path (main/resources/data)into your database. 

2- Or you can make a trip through the functionality of sales, so you can get all the links and APIs using swagger documentation:

	http://localhost:8080/swagger-ui/index.html [GET]

     

## Building for production

### Packaging as jar

To build the final jar and optimize the SalesManager application for production, run:

```

./mvnw -Pprod clean verify


```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```

java -jar target/*.jar


```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.


### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify


```

## Testing

To launch your application's tests, run:

```
./mvnw verify
or

mvn test spring-boot:run
```

For more information, refer to the Running tests page.

