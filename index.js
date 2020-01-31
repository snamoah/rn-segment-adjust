var ReactNative = require('react-native')
var disabled =
	ReactNative.Platform.OS === 'ios'
		? 'false' === 'true'
		: ReactNative.Platform.OS === 'android'
		? 'false' === 'true'
		: true

if (disabled) {
	module.exports = { disabled: true }
} else {
	var bridge = ReactNative.NativeModules['RNAnalyticsIntegration_Adjust']

	if (!bridge) {
		throw new Error('Failed to load Adjust integration native module')
	}

	bridge.setup.setupListener = bridge.setupListener
	bridge.setup.getAttribution = bridge.getAttribution
	module.exports = bridge.setup
}
