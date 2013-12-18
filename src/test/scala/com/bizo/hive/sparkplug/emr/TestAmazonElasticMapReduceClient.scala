package com.bizo.hive.sparkplug.emr

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.model._
import com.amazonaws.ResponseMetadata
import com.amazonaws.AmazonWebServiceRequest
import java.util.UUID
import com.amazonaws.regions.Region

class TestAmazonElasticMapReduceClient extends AmazonElasticMapReduce {
  var jobFlowRequest: RunJobFlowRequest = null

  def runJobFlow(runJobFlowRequest: RunJobFlowRequest): RunJobFlowResult = {

    jobFlowRequest = runJobFlowRequest

    new RunJobFlowResult().withJobFlowId(UUID.randomUUID.toString)
  }

  def addInstanceGroups(addInstanceGroupsRequest: AddInstanceGroupsRequest): AddInstanceGroupsResult = ???
  def addJobFlowSteps(addJobFlowStepsRequest: AddJobFlowStepsRequest) = ???
  def terminateJobFlows(terminateJobFlowsRequest: TerminateJobFlowsRequest) = ???
  def describeJobFlows(describeJobFlowsRequest: DescribeJobFlowsRequest): DescribeJobFlowsResult = ???
  def setTerminationProtection(setTerminationProtectionRequest: SetTerminationProtectionRequest) = ???
  def modifyInstanceGroups(modifyInstanceGroupsRequest: ModifyInstanceGroupsRequest) = ???
  def describeJobFlows(): DescribeJobFlowsResult = ???
  def modifyInstanceGroups() = ???
  def shutdown() = ???
  def setEndpoint(endpoint: String) = ???
  def getCachedResponseMetadata(request: AmazonWebServiceRequest): ResponseMetadata = ???
  def setVisibleToAllUsers(visibleToAllUsers: SetVisibleToAllUsersRequest) = ???
  def describeCluster(): DescribeClusterResult = ???
  def describeCluster(describeClusterRequest: DescribeClusterRequest): DescribeClusterResult = ???
  def describeStep(): DescribeStepResult = ???
  def describeStep(describeStepRequest: DescribeStepRequest): DescribeStepResult = ???
  def listBootstrapActions(): ListBootstrapActionsResult = ???
  def listBootstrapActions(listBootstrapActionsRequest: ListBootstrapActionsRequest): ListBootstrapActionsResult = ???
  def listClusters(): ListClustersResult = ???
  def listClusters(listClustersRequest: ListClustersRequest): ListClustersResult = ???
  def listInstanceGroups(): ListInstanceGroupsResult = ???
  def listInstanceGroups(listInstanceGroupsRequest: ListInstanceGroupsRequest): ListInstanceGroupsResult = ???
  def listInstances(): ListInstancesResult = ???
  def listInstances(listInstancesRequest: ListInstancesRequest): ListInstancesResult = ???
  def listSteps(): ListStepsResult = ???
  def listSteps(listStepsRequest: ListStepsRequest): ListStepsResult = ???
  def setRegion(region: Region): Unit = ???  
}