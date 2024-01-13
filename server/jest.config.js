module.exports = {
  clearMocks: true,
  collectCoverage: true,
  coverageDirectory: 'coverage',
  coverageProvider: 'v8',
  testEnvironment: 'node',
  testResultsProcessor: 'jest-sonar-reporter',
  collectCoverageFrom: [
    'src/**',
    '!**/node_modules/**',
    '!**/src/constants/**',
    '!**/src/config/**',
  ],
  modulePathIgnorePatterns: [
    'src/constants'
  ],
};
