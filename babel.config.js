module.exports = {
  presets: [
    'module:metro-react-native-babel-preset',
    'module:react-native-dotenv',
  ],
}

/*
const pathToRNEvernymSdk = require('./package.json').dependencies[
  '@evernym/react-native-white-label-app'
  ]

module.exports = {
  presets: [
    'module:metro-react-native-babel-preset',
    'module:react-native-dotenv',
  ],
  plugins: [
    ['@babel/plugin-proposal-decorators', { legacy: true }],
    [
      'module-resolver',
      {
        alias: {
          '@evernym/react-native-white-label-app': pathToRNEvernymSdk,
        },
      },
    ],
  ],
}
*/
