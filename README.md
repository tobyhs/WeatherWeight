# Weather Weight

Note: This app no longer works because Yahoo retired their YQL APIs. I might
try a different weather API later.

Weather Weight is a lightweight weather app for Android. It uses Yahoo's
weather API.

Reasons for developing:
* Popular weather apps I tried were on the heavy side with respect to features
  and resource usage, and I wanted something lighter
* I wanted to try building an Android app with some common libraries and
  practices; as a result, some libraries and approaches used might be overkill
  for this app

## Development

You will need to create a `secret.properties` file in the project root with
your AccuWeather API key:
```gradle
accuweatherApiKey=abc123
```
