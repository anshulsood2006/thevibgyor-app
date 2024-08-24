import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import './Home.css';
import Home1 from './configurations/Home1';
import UserConfiguration from './configurations/UserConfiguration';
import DictionaryConfiguration from './configurations/DictionaryConfiguration';

const components = {
  home1: Home1,
  userConfiguration: UserConfiguration,
  dictionaryConfiguration: DictionaryConfiguration
};

const Home = () => {
  const [authenticated, setAuthenticated] = useState(null);
  const [roles, setRoles] = useState([]);
  const [menuItems, setMenuItems] = useState([]);
  const [subMenuItems, setSubMenuItems] = useState([]);
  const [selectedComponent, setSelectedComponent] = useState(localStorage.getItem('selectedComponent') || null);
  const [selectedSubMenu, setSelectedSubMenu] = useState(localStorage.getItem('selectedSubMenu') || null);
  const [burgerMenuVisible, setBurgerMenuVisible] = useState(false);
  const [activeMenuItemId, setActiveMenuItemId] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token"); // Retrieve the token from localStorage
    if (token) {
      try {
        const decodedToken = jwtDecode(token); // Decode the token
        setRoles(decodedToken.roles || []); // Extract roles
        setAuthenticated(true);
        populateMenuItems(token);
      } catch (error) {
        console.error("Invalid token");
        setAuthenticated(false);
      }
    } else {
      setAuthenticated(false);
    }
  }, []);

  useEffect(() => {
    if (menuItems.length > 0) {
      let initialComponent = selectedComponent;
      if (!initialComponent) {
        initialComponent = menuItems[0].componentName;
        setSelectedComponent(initialComponent);
      }
      let initialSubMenu = selectedSubMenu;
      if(!initialSubMenu){
        initialSubMenu = menuItems.find(item => item.componentName === initialComponent);
        setSelectedSubMenu(initialSubMenu.subMenu[0].componentName);
      }
      const parentMenu = parentMenuItemByComponentName(menuItems, selectedSubMenu);
      //This check is added to set the active menu item id only in case parentMenu is non null
      if (parentMenu){
        setActiveMenuItemId(parentMenu.id);
      }
      setSelectedComponent(selectedSubMenu ? selectedSubMenu : []);
    }
  }, [menuItems]);

  const parentMenuItemByComponentName = (menuItems, subMenuItemComponentName) => {
      return menuItems.find(item => {
        return item.subMenu.find(subItem => subItem.componentName === subMenuItemComponentName);
      });
  };

  const parentMenuItem = (menuItems, subMenuItem) => {
      return menuItems.find(item => {
        return item.subMenu.find(subItem => subItem.componentName === subMenuItem.componentName);
      });
  };

  const handleSubMenuClick = (subMenuItem, event) => {
    event.preventDefault();
    setSelectedComponent(subMenuItem.componentName);
    localStorage.setItem('selectedSubMenu', subMenuItem.componentName);
    window.history.pushState({}, "", `/${subMenuItem.componentName}`);
    const parentMenu = parentMenuItem(menuItems, subMenuItem);
    setActiveMenuItemId(parentMenu.id);
  }

  const handleMenuItemClick = (menuItem, event) => {
    event.preventDefault();
    setSelectedComponent(menuItem.componentName); // Changed to set component name
    setSubMenuItems(menuItem.subMenu || []);
    setActiveMenuItemId(menuItem.id);
    localStorage.removeItem("selectedSubMenu");
    localStorage.setItem('selectedComponent', menuItem.componentName);
    window.history.pushState({}, "", `/${menuItem.componentName}`);
  }

  const populateMenuItems = (token) => {
    fetch(`${process.env.BACKEND_API_BASE_URL}${process.env.APP}${process.env.HEADER_MENUS}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    .then(response => response.json())
    .then(data => {
      setMenuItems(data.response.data);
      // Set initial submenus based on the first menu item
      if (data.response.data.length > 0) {
        const initialComponent = localStorage.getItem('selectedComponent') || data.response.data[0].componentName;
        setSelectedComponent(initialComponent);
        const selectedMenuItem = data.response.data.find(item => item.componentName === initialComponent);
        setSubMenuItems(selectedMenuItem ? selectedMenuItem.subMenu : []);
        localStorage.setItem('selectedComponent', initialComponent);
      }
    })
    .catch(error => console.error('Error fetching menu items:', error))
  }

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("selectedSubMenu");
    localStorage.removeItem("selectedComponent");
    setAuthenticated(false);
  };

  const renderContent = () => {
    if (!selectedComponent) return <p>Select a submenu to view content</p>;
    const Component = components[selectedComponent] || null;
    return Component ? <Component /> : <p>Component not found</p>;
  };

  if (authenticated === null) {
    return <div>Loading...</div>;
  }

  if (!authenticated) {
    return <Navigate replace to="/login" />;
  }

  const toggleBurgerMenu = () => {
    setBurgerMenuVisible(!burgerMenuVisible);
  };

  return (
    <div className="gridContainer">
      <header className="header">
        <nav className="navLinks">
          {menuItems.map(item => (
            <a
              key={item.id}
              href={`/${item.componentName}`}
              onClick={(event) => handleMenuItemClick(item, event)}
              className={selectedComponent === item.componentName || activeMenuItemId === item.id ? 'active' : ''}>
              {item.displayName}
            </a>
          ))}
        </nav>
        <div className="burgerMenu">
          <button onClick={toggleBurgerMenu} className="burgerMenuButton">☰</button>
          {burgerMenuVisible && (
            <div className="burgerMenuContent">
              <div className="rolesDisplay">Your roles: {roles.join(", ")}</div>
              <a href="/profile">Profile</a>
              <a href="/settings">Settings</a>
              <a href="/login" onClick={handleLogout}>Logout</a>
            </div>
          )}
        </div>
      </header>
      <aside className="sidebar">
        <ul>
          {subMenuItems.map(subMenuItem => (
            <li key={subMenuItem.id}>
              <a
                href={`/${subMenuItem.componentName}`}
                onClick={(event) => handleSubMenuClick(subMenuItem, event)}>
                {subMenuItem.displayName}
              </a>
            </li>
          ))}
        </ul>
      </aside>
      <main className="content">
        {renderContent()}
      </main>
    </div>
  );
};

export default Home;