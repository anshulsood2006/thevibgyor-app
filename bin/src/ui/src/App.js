import './App.css';
import Home from './components/Home'
import Login from './components/Login'
import Example from './components/configurations/Example'
import { Routes, Route, BrowserRouter } from "react-router-dom"

function App() {
  return (
    <div>
        <BrowserRouter>
               <Routes>
                   <Route path="/login" element={<Login />} />
                   <Route path="/*" element={<Home />} />
                   <Route path="/example" element={<Example />} />
               </Routes>
             </BrowserRouter>
    </div>
  );
}
export default App;