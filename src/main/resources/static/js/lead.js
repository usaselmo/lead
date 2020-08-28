/*
 * ANGULARJS 
 */
 var local_server_url = "";

 angular.module('lead', [])

 .service('userService', function($http){
 	return {
 		findEstimators: function (scope) {
 			$http.get(local_server_url + "/users/estimators").then(function (response) {
 				scope.estimators = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 .service('personService', function($http){
 	return {
 		findPersons: function (scope) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.persons = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 .service('companyService', function($http){
 	return {
 		findCompanies: function (scope) {
 			$http.get(local_server_url + "/companies").then(function (response) {
 				scope.companies = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 .service('leadService', function($http){
 	return {
 		findLeads: function (scope, pageRange, lines) {
 			if(scope.event=='ALL' || !scope.event) scope.event='';
 			if(!scope.search) scope.search = '';
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&event=" + scope.event + "&text=" + scope.search).then(function (response) {
 				scope.leads = response.data.leads
 				scope.leadsTotalPrice = response.data.leadsTotalPrice
 				scope.totalLeads = response.data.totalLeads
 				scope.leadTypes = response.data.leadTypes;
 				scope.events = response.data.events;
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, lead) {
 			$http.put(local_server_url + "/leads", lead).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.lead = response.data.lead
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, lead) {
 			$http.post(local_server_url + "/leads", lead).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.lead = response.data.lead
 					scope.successMessages = response.data.successMessages
 				}   
 			}, function (response) {
 				console.log(response)            
 			});
 		},
 		
 		fireEvent: function (scope, lead, event) {
 			$http.post(local_server_url + "/leads/"+lead.id+"/fireevent", event).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					if(scope.leads){
 						scope.leads = scope.leads.map(lead => lead.id==response.data.lead.id?response.data.lead:lead);
 					}else{
 						scope.lead = response.data.lead
 					}
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		saveNote: function (scope, lead, note) {
 			$http.post(local_server_url + '/leads/' + lead.id + '/addNote', note).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.successMessages = response.data.successMessages
 					scope.lead = response.data.lead
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},

 	} 
 })


 /*MAIN CONTROLLER*/
 .controller('LeadController', function ($scope, leadService, companyService, personService, userService) {  
 	/** CRUD **/
 	$scope.crud = function(lead){
 		$scope.crudLead = lead;
 		$scope.leads = null;
 		$scope.lead = null;
 		companyService.findCompanies($scope)
 		personService.findPersons($scope)
 		userService.findEstimators($scope)
 	}

 	$scope.save = function(lead){
 		if(lead.id)
 			leadService.update($scope, lead)
 		else
 			leadService.save($scope, lead);
 		$scope.cancel(lead)
 	}

 	$scope.cancel = function(lead){
 		if(lead.id)
 			$scope.detail(lead)
 		else
 			$scope.list()
 	}

 	/** DETAIL **/
 	$scope.detail = function(lead){
 		$scope.lead = lead;
 		$scope.crudLead = null;
 		$scope.leads = null;
 	}

 	$scope.fireEvent = function(lead, event){
 		leadService.fireEvent($scope, lead, event)
 	}

 	$scope.saveNote = function(lead, newNote){
 		leadService.saveNote($scope, lead, newNote)
 	}

 	/** LIST **/
 	var pageRange = 0;
 	var lines = 10;

 	$scope.list = function(){
 		$scope.lead = null;
 		$scope.crudLead = null;
 		$scope.reload($scope.event, $scope.search);
 	}

 	$scope.getNextListRange = function(numero){
 		if(numero>0 && ((pageRange+1)*lines) < $scope.totalLeads)
 			pageRange++
 		if(numero<0 && pageRange>0)
 			pageRange--
 		leadService.findLeads($scope, pageRange, lines, $scope.event, $scope.search)
 	} 

 	$scope.reload = function(event, search){
 		$scope.event = event;
 		$scope.search = search;
 		pageRange = 0;
 		leadService.findLeads($scope, pageRange, lines)
 	}

 	var init = function(){
 		leadService.findLeads($scope, pageRange, lines);
 	}  

 	init();

 });

