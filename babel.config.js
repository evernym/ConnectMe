module.exports = {
  presets: [
    'module:metro-react-native-babel-preset',
    'module:react-native-dotenv',
  ],
}

/*
const pathToRNEvernymSdk = require('./package.json').dependencies[
  '@dev/react-native-evernym-sdk'
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
          '@dev/react-native-evernym-sdk': pathToRNEvernymSdk,
        },
      },
    ],
  ],
}
*/
