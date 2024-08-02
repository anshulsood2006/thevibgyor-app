db = db.getSiblingDB('thevibgyor');

// Enum values for roles
const rolesEnum = ["ADMIN", "USER"];

db.user_roles.insertMany([
    { id: 0, role_name: 'SUPER_ADMIN'},
    { id: 1, role_name: 'ADMIN'},
    { id: 2, role_name: 'USER'},
    { id: 3, role_name: 'GUEST'}
]);

db.users.insertMany([
    { id: 1, username: 'super@gmail.com', user_id: 'super@gmail.com', email: 'super@gmail.com', role_ids: [0,1,2,3], password: '$2a$10$HiBPsAxxUCKdyfCvG3NKIOzG9uB0k8FU8MSWwkvdbFbfYB91EbDMa'},
    { id: 2, username: 'anshul@gmail.com', user_id: 'anshul@gmail.com', email: 'anshul@gmail.com', role_ids: [1,2,3], password: '$2a$10$HiBPsAxxUCKdyfCvG3NKIOzG9uB0k8FU8MSWwkvdbFbfYB91EbDMa'},
    { id: 3, username: 'akhil@gmail.com', user_id: 'akhil@gmail.com', email: 'akhil@gmail.com', role_ids: [2,3], password: '$2a$10$HiBPsAxxUCKdyfCvG3NKIOzG9uB0k8FU8MSWwkvdbFbfYB91EbDMa'},
    { id: 4, username: 'ruhaan@gmail.com', user_id: 'ruhaan@gmail.com', email: 'ruhaan@gmail.com', role_ids: [2,3], password: '$2a$10$HiBPsAxxUCKdyfCvG3NKIOzG9uB0k8FU8MSWwkvdbFbfYB91EbDMa'}
]);

db.sub_menu.insertMany([
    {id:1, display_name: 'Home Menu 1', component_name: 'home1'},
    {id:2, display_name: 'Home Menu 2', component_name: 'home2'},
    {id:3, display_name: 'About Us 1', component_name: 'about1'},
    {id:4, display_name: 'About Us 2', component_name: 'about1'},
    {id:5, display_name: 'Privacy Policy 1', component_name: 'privacy1'},
    {id:6, display_name: 'Privacy Policy 2', component_name: 'privacy2'},
    {id:7, display_name: 'Contact Us 1', component_name: 'contact1'},
    {id:8, display_name: 'Contact Us 2', component_name: 'contact2'},
    {id:9, display_name: 'Administer 1', component_name: 'admin1'},
    {id:10, display_name: 'Administer 2', component_name: 'admin2'},
    {id:11, display_name: 'User Profile 1', component_name: 'user1'},
    {id:12, display_name: 'User Profile 2', component_name: 'user2'},
    {id:13, display_name: 'User Configuration', component_name: 'userConfiguration'},
    {id:14, display_name: 'Dictionary Configuration', component_name: 'dictionaryConfiguration'}
]);

db.menu.insertMany([
    { id: '1', display_name: 'Home', component_name: 'home1', sub_menu_ids: [1,2], role_ids: [0,1,2,3]},
    { id: '2', display_name: 'About Us', component_name: 'about', sub_menu_ids: [3,4], role_ids: [0,1,2,3]},
    { id: '3', display_name: 'Privacy Policy', component_name: 'privacy', sub_menu_ids: [5,6], role_ids: [0,1,2,3]},
    { id: '4', display_name: 'Contact Us', component_name: 'contact', sub_menu_ids: [7,8], role_ids: [0,1,2,3]},
    { id: '5', display_name: 'Administer', component_name: 'admin', sub_menu_ids: [9,10], role_ids: [0,1]},
    { id: '6', display_name: 'User Profile', component_name: 'user', sub_menu_ids: [11,12], role_ids: [0,1,2]},
    { id: '7', display_name: 'Configurations', component_name: 'userConfiguration', sub_menu_ids: [13,14], role_ids: [0]},
]);
