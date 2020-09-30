import invitation from '/app/module/lead/invitation/lead-invitation.js'
import proposalMedia from '/app/module/lead/media/lead-media.js'
import proposalNote from '/app/module/lead/note/lead-note.js'
import proposalEvent from '/app/module/lead/event/lead-event.js'
import proposalCrud from '/app/module/lead/proposal/lead-proposal.js'
import loading from '/app/module/loading/loading.js'
import personCrud from '/app/module/person/person-crud.js'
import modal from '/app/module/modal/modal.js'
import Filter from '/app/model/filter.js'

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

	$scope.filter = new Filter(0, 5, '', '');

	$scope.leadEntity = {};

	var findLeads = function () {
		if ($scope.filter.event == 'ALL' || !$scope.filter.event) $scope.filter.event = '';
		if (!$scope.filter.searchText) $scope.filter.searchText = '';
		leadService.find_Leads($scope).success(data => {
			$scope.leads = data.leads
			$scope.leadEntity.leadsTotalPrice = data.leadsTotalPrice
			$scope.leadEntity.totalLeads = data.totalLeads
			$scope.leadTypes = data.leadTypes;
			$scope.events = data.events;
		})
	}

	$scope.createNewPerson = function () {
		$scope.person = {};
	}

	$scope.list = function () {
		$scope.lead = null;
		$scope.crudLead = null;
		$scope.reload($scope.filter.event, $scope.filter.searchText);
	}

	$scope.getNextListRange = function (numero) {
		if (numero > 0 && (($scope.filter.pageRange + 1) * $scope.filter.lines) < $scope.leadEntity.totalLeads)
			$scope.filter.pageRange++
		else if (numero < 0 && $scope.filter.pageRange > 0)
			$scope.filter.pageRange--
		else
			return
		$scope.leads = null;
		findLeads($scope)
	}

	$scope.reload = function (event, search) {
		$scope.filter.event = event;
		$scope.filter.searchText = search;
		$scope.filter.pageRange = 0;
		$scope.leads = null;
		findLeads($scope)
	}

	$scope.applyFilter = function (event, search) {
		$scope.filter.pageRange = 0;
		findLeads($scope)
	}

	var init = function () {
		findLeads($scope)
	}

	init();

	$scope.crud = function (lead) {
		$scope.crudLead = lead;
		$scope.leads = null;
		$scope.lead = null;
		companyService.findCompanies($scope)
		personService.findPersons($scope)
		userService.findEstimators($scope)
	}

	$scope.save = function (lead) {
		if (lead.id)
			leadService.update($scope, lead)
		else
			leadService.save($scope, lead);
		$scope.cancel(lead)
	}

	$scope.cancel = function (lead) {
		if (lead.id)
			$scope.detail(lead)
		else
			$scope.list()
	}

	$scope.companyOnChange = function (company) {
		personService.findPersons($scope)
			.success(() => {
				var persons = $scope.persons
					.filter(p => p.company && p.company.id && p.company.id == company.id);
				if (persons.length > 0)
					$scope.persons = persons;
			});
	}

	$scope.detail = function (lead) {
		$scope.lead = lead;
		$scope.crudLead = null;
		$scope.leads = null;
	}

	$scope.fireEvent = function (lead, event) {
		leadService.fireEvent($scope, lead, event)
	}

	$scope.saveNote = function (lead, newNote) {
		leadService.saveNote($scope, lead, newNote)
	}

	$scope.sendCantReachEmail = function (lead, person) {
		if (person && confirm('Send Can\'t Reach E-mail to ' + person.name + ' ? ')) {
			personService.sendCantReachEmail(lead, person);
		}
	}

	$scope.sendHiringDecisionEmail = function (lead, person) {
		if (person && confirm('Send e-mail asking about ' + person.name + '\'s  hiring decision ? ')) {
			personService.sendHiringDecisionEmail(lead, person);
		}
	}

	$scope.reloadLead = function (lead) {
		leadService.findLead(lead.id).success(data => $scope.lead = data.lead)
	}

} 
