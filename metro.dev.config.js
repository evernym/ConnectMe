/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */
const path = require('path')
const pathToRNEvernymSdk = require('./package.json').dependencies[
  '@dev/react-native-evernym-sdk'
]

module.exports = {
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
  server: {
    port: 8081,
  },
  projectRoot: __dirname,
  watchFolders: [path.resolve(__dirname, pathToRNEvernymSdk)],
  resolver: {
    extraNodeModules: new Proxy(
      {},
      {
        get: (target, name) => {
          if (target.hasOwnProperty(name)) {
            return target[name]
          }
          return path.join(process.cwd(), `node_modules/${name}`)
        },
      }
    ),
  },
}
