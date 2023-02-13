# Currency Converter

[Demo Video](https://youtu.be/O8s3Bd3pNNM)  

## [Goal]

#### Develop a Currency Conversion App that allows a user view exchange rates for any given currency

- [ ] Create a Project for a Mobile Phone
- [ ] Android: _Kotlin_ | iOS: _Swift_ (sorry, no Objective-C or Java please! You can learn Kotlin/Swift easily I'm sure:))

#### Functional Requirements:
- [ ] Exchange rates must be fetched from: https://docs.openexchangerates.org/reference/api-introduction  
- [ ] Use free API Access Key for using the API
- [ ] User must be able to select a currency from a list of currencies provided by the API (for currencies that are not available, convert them on the app side. When converting, floating-point error is accpetable)
- [ ] User must be able to enter desired amount for selected currency
- [ ] User should then see a list of exchange rates for the selected currency
- [ ] Rates should be persisted locally and refreshed no more frequently than every 30 minutes (to limit bandwidth usage)
- [ ] Write unit testing

#### UI Suggestion:
- [ ] Some way to select a currency
- [ ] Some text entry widget to enter the amount
- [ ] A list/grid of exchange rates
- [ ] It doesn’t need to be super pretty, but it shouldn’t be broken as well  

![UI Suggested Wireframe](ui_suggestion.png)



# Main Libraries Used
* Compose
* Coroutines
* Room
* Dagger Hilt
* Retrofit
* OkHttp3
* Gson
* Junit
