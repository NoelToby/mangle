/*
 * Copyright (c) 2016-2019 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.mangle.unittest.utils.clients.docker;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.vmware.mangle.utils.clients.docker.CustomDockerClient;
import com.vmware.mangle.utils.clients.docker.DockerOperations;
import com.vmware.mangle.utils.mockdata.DockerMockdata;


/**
 * Unit Test Case for DockerOperations.
 *
 * @author rpraveen
 */
@PrepareForTest(value = { DockerClient.class, ExposedPort.class })
@PowerMockIgnore(value = { "javax.net.ssl.*" })
public class DockerOperationsTest extends PowerMockTestCase {

    @InjectMocks
    private CustomDockerClient customDockerClient;
    @Mock
    private DockerClient dockerClient;

    private DockerMockdata mockdata = new DockerMockdata();

    private String host;
    private String port;
    private String containerName;
    private String containerId;

    /**
     * @throws Exception
     */
    @BeforeClass
    public void setUpBeforeClass() throws Exception {
        MockitoAnnotations.initMocks(this);
        new CustomDockerClient(host, port, true);
        customDockerClient.setDockerClient(dockerClient);
        host = mockdata.getMockHost();
        port = mockdata.getMockPort();
        containerName = mockdata.getMockContainerName();
        containerId = mockdata.getMockContainerId();
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public void tearDownAfterClass() throws Exception {
        this.dockerClient = null;
    }

    /**
     * @throws Exception
     */
    @AfterMethod
    public void tearDown() throws Exception {
        validateMockitoUsage();
    }

    /**
     * Test method for {@link#MangleDockerOperations#dockerPause }.
     *
     * @throws Exception
     */
    @Test
    public void testdockerPause() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withStatusFilter(anyString())).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allContainers = new ArrayList<>();
        allContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(containerId);

        PauseContainerCmd pauseContainercmd = mock(PauseContainerCmd.class);
        when(customDockerClient.getDockerClient().pauseContainerCmd(containerId)).thenReturn(pauseContainercmd);

        ListContainersCmd listContainersCmd1 = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd1);
        when(listContainersCmd1.withStatusFilter(anyString())).thenReturn(listContainersCmd1);
        Container container1 = mock(Container.class);
        List<Container> allContainers1 = new ArrayList<>();
        allContainers1.add(container1);
        when(listContainersCmd1.exec()).thenReturn(allContainers1);
        when(container1.getNames()).thenReturn(new String[] { containerName });
        when(container1.getId()).thenReturn(containerId);

        try {
            Assert.assertEquals(DockerOperations.dockerPause(customDockerClient, containerName).getExitCode(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(dockerClient, times(2)).listContainersCmd();

    }

    /**
     * Test method for {@link#MangleDockerOperations#dockerPause )}.
     *
     * @throws Exception
     */
    @Test
    public void testdockerUnPause() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withStatusFilter(anyString())).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allpausedContainers = new ArrayList<>();
        allpausedContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allpausedContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(containerId);

        UnpauseContainerCmd unpauseContainercmd = mock(UnpauseContainerCmd.class);
        when(customDockerClient.getDockerClient().unpauseContainerCmd(containerId)).thenReturn(unpauseContainercmd);
        try {
            Assert.assertEquals(DockerOperations.dockerUnPause(customDockerClient, containerName).getExitCode(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(dockerClient, times(2)).listContainersCmd();
    }

    /**
     * Test method for {@link#MangleDockerOperations#dockerPause )}.
     *
     * @throws Exception
     */
    @Test
    public void testdockerStart() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allContainers = new ArrayList<>();
        allContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(containerId);

        StartContainerCmd startContainerCmd = mock(StartContainerCmd.class);
        when(customDockerClient.getDockerClient().startContainerCmd(containerId)).thenReturn(startContainerCmd);

        try {
            Assert.assertEquals(
                    DockerOperations.dockerStart(customDockerClient, containerName, containerId).getExitCode(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(dockerClient, times(1)).listContainersCmd();
    }

    /**
     * Test method for {@link#MangleDockerOperations#dockerPause )}.
     *
     * @throws Exception
     */
    @Test
    public void testdockerStop() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withStatusFilter(anyString())).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allpausedContainers = new ArrayList<>();
        allpausedContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allpausedContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(containerId);

        StopContainerCmd stopContainerCmd = mock(StopContainerCmd.class);
        when(customDockerClient.getDockerClient().stopContainerCmd(containerId)).thenReturn(stopContainerCmd);

        try {
            Assert.assertEquals(DockerOperations.dockerStop(customDockerClient, containerName).getExitCode(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(dockerClient, times(2)).listContainersCmd();
    }


    @Test(description = "test to verify the failure of docker start operation with an exception")
    public void testdockerStart_V2() throws Exception {

        try {
            Assert.assertEquals(
                    DockerOperations.dockerStart(customDockerClient, containerName, null).getExitCode(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(description = "test to verify the failure of docker stop operation with an exception")
    public void testdockerStop_V2() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withStatusFilter(anyString())).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allContainers = new ArrayList<>();
        allContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(null);
        try {
            Assert.assertEquals(DockerOperations.dockerStop(customDockerClient, containerName).getExitCode(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "test to verify the failure of docker unpause operation with an exception")
    public void testdockerUnpause_V2() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withStatusFilter(anyString())).thenReturn(listContainersCmd);
        Container container = mock(Container.class);
        List<Container> allContainers = new ArrayList<>();
        allContainers.add(container);
        when(listContainersCmd.exec()).thenReturn(allContainers);
        when(container.getNames()).thenReturn(new String[] { containerName });
        when(container.getId()).thenReturn(null);
        try {
            Assert.assertEquals(DockerOperations.dockerUnPause(customDockerClient, containerName).getExitCode(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(description = "test to verify the failure of docker pause operation with an exception")
    public void testdockerPause_V2() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        List<Container> allContainers = new ArrayList<>();
        allContainers.clear();
        when(listContainersCmd.exec()).thenReturn(allContainers);
        try {
            Assert.assertEquals(DockerOperations.dockerPause(customDockerClient, containerName).getExitCode(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(description = "test to verify the failure of docker unpause operation with an exception")
    public void testdockerUnPause_V3() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        List<Container> allContainers = new ArrayList<>();
        allContainers.clear();
        when(listContainersCmd.exec()).thenReturn(allContainers);

        try {
            Assert.assertEquals(DockerOperations.dockerUnPause(customDockerClient, containerName).getExitCode(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(description = "test to verify the failure of docker stop operation with an exception")
    public void testdockerStop_V3() throws Exception {
        ListContainersCmd listContainersCmd = mock(ListContainersCmd.class);
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        List<Container> allContainers = new ArrayList<>();
        allContainers.clear();
        when(listContainersCmd.exec()).thenReturn(allContainers);

        try {
            Assert.assertEquals(DockerOperations.dockerStop(customDockerClient, containerName).getExitCode(),
                    1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
