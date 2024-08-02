const { override } = require('customize-cra');
const webpack = require('webpack');
const dotenv = require('dotenv');

module.exports = override(config => {
  // Load environment variables from .env file
  const env = dotenv.config().parsed;

  // Create an array of environment variables in the form of "REACT_APP_<name>"
  const envKeys = Object.keys(env).reduce((prev, next) => {
    prev[`process.env.${next}`] = JSON.stringify(env[next]);
    return prev;
  }, {});

  // Add the environment variables to the DefinePlugin
  config.plugins.push(new webpack.DefinePlugin(envKeys));

  return config;
});
