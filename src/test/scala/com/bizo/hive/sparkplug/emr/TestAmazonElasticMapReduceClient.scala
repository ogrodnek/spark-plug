package com.bizo.hive.sparkplug.emr

import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elasticmapreduce.model._
import com.amazonaws.ResponseMetadata
import com.amazonaws.AmazonWebServiceRequest
import java.util.UUID

class TestAmazonElasticMapReduceClient extends AmazonElasticMapReduce {
  var jobFlowRequest: RunJobFlowRequest = null

  def runJobFlow(runJobFlowRequest: RunJobFlowRequest): RunJobFlowResult = {

    jobFlowRequest = runJobFlowRequest

    new RunJobFlowResult().withJobFlowId(UUID.randomUUID.toString)
  }

  def ??? = sys.error("Not implemented")

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
}