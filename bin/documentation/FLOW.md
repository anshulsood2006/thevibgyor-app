| SN | Step                                      |
|----|-------------------------------------------|
|    | During application Start up               |
| 1  | All beans are initialized                 |
| 2  | All security configuration is initialized |
|    | When an end point is called               |
| 1  | Spring Dispatcher servlet is called       |
| 2  | Servlet Dispatcher servlet is called      |
| 3  | Filter is called                          |
| 4  | Controller is called                      |
