// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: true,
  basePath: 'http://localhost:8768/', // 部署
  // basePath: '', // 本地
  // paperPath: window.location.host + '/starmoocexampaper'
  paperPath: 'http://127.0.0.1:8081/starmoocexampaper/',
  // paperPath: 'http://127.0.0.1:9000/starmoocexampaper/'
};
