# Currency Converter

## [Goal]

#### Develop a Currency Conversion App that allows a user view exchange rates for any given currency

- [ ] Create a Project for a Mobile Phone
- [ ] Android: _Kotlin_ | iOS: _Swift_ (sorry, no Objective-C or Java please! You can learn Kotlin/Swift easily I'm sure:))

#### Functional Requirements:
- [ ] Exchange rates must be fetched from: https://docs.openexchangerates.org/reference/api-introduction  
- [ ] Use free API Access Key for using the API
- [ ] User must be able to select a currency from a list of currencies provided by the API(for currencies that are not available, convert them on the app side. When converting, floating-point error is accpetable)
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

### What we're looking for:
- [ ] An App that meets the Functional Requirements above
- [ ] Your coding style! Show us how you like to write your code
- [ ] Architecture, how you've structured your code
- [ ] Principles, how you belive code should be written
- [ ] Qualities, how you guarantee your code is functioning



## [Funtional Requirement]  
Implement all the requirements and UI suggestion with MVVM structure, support Dark Mode.  
Please refer to demo video if necessary → [Demo Video](https://youtu.be/O8s3Bd3pNNM)  
<br>

## [Unit Test]  
Implement JUnit Compose Test.  
(Due to limited time, it is unlikely to include detail cases)  
Have checked that Logs are all fine in app classes related to each feature.  
Also, have checked that the whole app passes End-to-End Test.  
<br>

# Main Libraries Used
* Compose
* Coroutines
* Room
* Dagger Hilt
* Retrofit
* OkHttp3
* Gson
* Junit
