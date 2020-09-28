var loading = angular.module('app.module.loading', []);

loading.directive('appLoading', function () {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: '/app/module/loading/loading.html',
		controller: function ($scope) {
			$scope.volume = 0

			function sleep(ms) {
				return new Promise(resolve => setTimeout(resolve, ms));
			}

			async function demo() {
				console.log('Taking a break...');
				await sleep(2000);
				console.log('Two seconds later, showing sleep in a loop...');

				// Sleep in loop
				for (let i = 0; i < 5; i++) {
					if (i === 3)
						await sleep(2000);
					console.log(i);
				}
			}

			demo();
		}
	};
});

export default loading
