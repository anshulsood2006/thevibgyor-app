import React, { useState }  from 'react'
import { Navigate } from "react-router-dom";

const Login = (props) => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [emailError, setEmailError] = useState('')
    const [passwordError, setPasswordError] = useState('')
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem(localStorage.getItem("authenticated")|| false));

    const onButtonClick = () => {
         // Set initial error values to empty
         setEmailError('')
         setPasswordError('')

         // Check if the user has entered both fields correctly
        if ('' === email) {
            setEmailError('Please enter your email')
            return
        }

        if (!/^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
            setEmailError('Please enter a valid email')
            return
        }

         if ('' === password) {
            setPasswordError('Please enter your password')
            return
          }

          if (password.length <= 7) {
            setPasswordError('The password must be 8 characters or longer')
            return
          }
          logIn()
      }

      // Log in a user using email and password
      const logIn = () => {
        var authorizationBasic = window.btoa(email + ':' + password);
        fetch(`${process.env.BACKEND_API_BASE_URL}${process.env.GET_TOKEN}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + authorizationBasic
          },
          body: JSON.stringify({ ttl: "3000000000" })
        })
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.error === undefined) {
                const token = data.token;
                setIsLoggedIn(true);
                localStorage.setItem('token', token);
            } else {
                setPasswordError(data.error.message);
            }
        })
        .catch(error => {
            setPasswordError('Network error while trying to connect to backend server')
            console.error('Error:', error);
        });
      }

    if (isLoggedIn) {
        return <Navigate replace to="/home" />;
    }

    return (
             <div className={'mainContainer'}>
                   <div className={'titleContainer'}>
                       <div>Login</div>
                   </div>
                   <br />
                   <div className={'inputContainer'}>
                       <input value={email} placeholder="Enter your username here" onChange={(ev) => setEmail(ev.target.value)} className={'inputBox'}/>
                       <label className="errorLabel">{emailError}</label>
                   </div>
                   <br />
                    <div className={'inputContainer'}>
                        <input type="password" value={password} placeholder="Enter your password here" onChange={(ev) => setPassword(ev.target.value)} className={'inputBox'}/>
                        <label className="errorLabel">{passwordError}</label>
                    </div>
                    <br />
                    <div>
                        <input className={'inputButton'} type="button" onClick={onButtonClick}  value={'Log in'} />
                    </div>
             </div>
    )
}

export default Login