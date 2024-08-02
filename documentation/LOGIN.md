### POC on creating Login Page in React

### Prerequisite Softwares

- node version 18.18.0 or above. You can upgrade using command ```brew install n && sudo n latest```

### Steps to run the POC on local

- Initialize the project using command ```npx create-react-app ui``` 
- Start the app in dev mode from inside ui directory using command ```npm start```
- Create a components directory under ui
- Add package below dependency to package.json to remove start up warning
```
"@babel/plugin-transform-private-property-in-object": "7.24.7"
```
- install package react-router-dom
```
npm i react-router-dom
```
- 
