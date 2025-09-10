# Weather Weight

Weather Weight is a lightweight weather app for Android. It uses [Tomorrow.io's
API](https://www.tomorrow.io/weather-api/).

Reasons for developing:
* Popular weather apps I tried were on the heavy side with respect to features
  and resource usage, and I wanted something lighter
* I wanted to try building an Android app with some common libraries and
  practices; as a result, some libraries and approaches used might be overkill
  for this app

## Development

You will need to create a `secret.properties` file in the project root with
your Tomorrow.io API key:
```gradle
tomorrowApiKey=abc123
```
