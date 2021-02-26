import invitation from '/app/module/lead/invitation/lead-invitation.js'
import proposalMedia from '/app/module/lead/media/lead-media.js'
import proposalNote from '/app/module/lead/note/lead-note.js'
import proposalEvent from '/app/module/lead/event/lead-event.js'
import proposalCrud from '/app/module/lead/proposal/lead-proposal.js'
import loading from '/app/module/loading/loading.js'
import personCrud from '/app/module/person/person-crud.js'
import modal from '/app/module/modal/modal.js'
import Filter from '/app/model/filter.js'
import email from '/app/module/email/email.js'

import companyService from '/app/service/company-service.js'
import leadService from '/app/service/lead-service.js'
import personService from '/app/service/person-service.js'
import userService from '/app/service/user-service.js'
import proposalService from '/app/service/proposal-service.js'


var applead = angular.module('app.module.lead', [
	'app.module.lead.proposal',
	'app.module.lead.note',
	'app.module.lead.event',
	'app.module.lead.media',
	'app.module.lead.invitation',
	'app.module.loading',
	'app.module.lead.person.crud',
	'app.module.modal',
	'app.module.email',
]);

applead.service('companyService', companyService);
applead.service('leadService', leadService);
applead.service('personService', personService);
applead.service('proposalService', proposalService);
applead.service('userService', userService);

applead.directive('appLead', function () {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: '/app/module/lead/lead.html',
		controller: leadController,
	};
});


export default applead

var leadController = function ($scope, leadService, companyService, personService, userService) {

	$scope.filter = new Filter(0, 100, '', '');
	$scope.lead = null;
	$scope.leads = {};

	var findLeads = function () {
		if ($scope.filter.event == 'ALL' || !$scope.filter.event) $scope.filter.event = '';
		if (!$scope.filter.searchText) $scope.filter.searchText = '';
		var res = leadService.find_Leads($scope.filter);
		$scope.searching = true;
		res.success(data => {
			$scope.leads = data.leads
			$scope.leadsTotalPrice = data.leadsTotalPrice
			$scope.totalLeads = data.totalLeads
			$scope.leadTypes = data.leadTypes;
			$scope.events = data.events;
			$scope.searching = false;
		})
		return res;
	}

	$scope.searching = false;
	$scope.applyFilter = function (event, search) {
		if (search.length > 2 || search.length == 0) {
			if (!$scope.searching) {
				$scope.searching = true;
				findLeads().success(data => {
					$scope.searching = false;
				})
			} else {
				setTimeout(() => $scope.applyFilter(event, search), 20);
			}
		}
	}

	$scope.createNewPerson = function (p) {
		$scope.person = p ? p : {};
	}


	$scope.list = function () {
		$scope.crudLead = null;
		if ($scope.lead.id)
			$scope.leads = $scope.leads.map(l => l.id == $scope.lead.id ? $scope.lead : l)
		$scope.lead = null;
	}

	$scope.getNextListRange = function (numero) {
		if (numero > 0 && (($scope.filter.pageRange + 1) * $scope.filter.lines) < $scope.totalLeads)
			$scope.filter.pageRange++
		else if (numero < 0 && $scope.filter.pageRange > 0)
			$scope.filter.pageRange--
		else
			return
		$scope.leads = null;
		findLeads($scope)
	}

	$scope.reload = function (event, search) {
		//$scope.filter.event = event;
		//$scope.filter.searchText = search;
		$scope.filter.pageRange = 0;
		$scope.leads = null;
		findLeads($scope)
	}

	var init = function () {
		findLeads($scope)
	}

	init();

	$scope.crud = function (lead) {
		$scope.crudLead = lead;
		//$scope.leads = null;
		$scope.lead = null;
		companyService.findCompanies($scope)
		personService.findPersons($scope)
		userService.findEstimators($scope)
	}

	$scope.save = function (lead) {
		if (lead.id)
			leadService.update(lead).success(data => lead = data.lead)
		else
			leadService.save(lead).success(data => {
				$scope.leads.unshift(data.lead);
				$scope.totalLeads++
			})
		$scope.cancel(lead)
	}


	$scope.copy = function (lead) {
		if (confirm('Create a new Lead based on this one?')) {
			lead.id = null;
			lead.eventLogs = [];
			lead.invitations = [];
			lead.medias = [];
			lead.proposals = [];

			leadService.save(lead).success(data => {
				$scope.leads.unshift(data.lead);
				$scope.totalLeads++
				$scope.lead = data.lead;
				alert('New Lead created successfully.')
				$scope.cancel($scope.lead)
			})
		}

	}



	$scope.cancel = function (lead) {
		if (lead.id)
			$scope.detail(lead)
		else {
			$scope.lead = null;
			$scope.crudLead = null;
		}
	}

	$scope.companyOnChange = function (company) {
		personService.findPersons($scope)
			.success(() => {
				var persons = $scope.persons.filter(p => p.company && p.company.id && p.company.id == company.id);
				if (persons.length > 0)
					$scope.persons = persons;
			});
	}

	$scope.detail = function (lead) {
		$scope.lead = lead;
		$scope.crudLead = null;
		//$scope.leads = null;
	}

	$scope.fireEvent = function (lead, event) {
		leadService.fireEvent(lead, event).success(data => {
			$scope.leads = $scope.leads.map(lead => lead.id == data.lead.id ? data.lead : lead);
		})
	}

	$scope.fireEventAndRefresh = function (lead, event) {
		leadService.fireEvent(lead, event).success(data => {
			$scope.leads = $scope.leads.map(lead => lead.id == data.lead.id ? data.lead : lead);
			$scope.lead = data.lead;
		})
	}

	$scope.saveNote = function (lead, newNote) {
		leadService.saveNote($scope, lead, newNote)
	}

	$scope.reloadLead = function (lead) {
		leadService.findLead(lead.id).success(data => $scope.lead = data.lead)
	}

} 
