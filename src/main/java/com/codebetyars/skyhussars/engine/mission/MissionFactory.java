/*
 * Copyright (c) 2016, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.codebetyars.skyhussars.engine.mission;

import com.codebetyars.skyhussars.engine.CameraManager;
import com.codebetyars.skyhussars.engine.DataManager;
import com.codebetyars.skyhussars.engine.DayLightWeatherManager;
import com.codebetyars.skyhussars.engine.GuiManager;
import com.codebetyars.skyhussars.engine.TerrainManager;
import com.codebetyars.skyhussars.engine.controls.ControlsManager;
import com.codebetyars.skyhussars.engine.controls.ControlsMapper;
import com.codebetyars.skyhussars.engine.plane.Plane;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class MissionFactory {

    private DataManager dataManager;
    private Node root;
    private ControlsMapper controlsMapper;
    private CameraManager cameraManager;
    private TerrainManager terrainManager;
    private GuiManager guiManager;
    private DayLightWeatherManager dayLightWeatherManager;

    public MissionFactory(DataManager dataManager, Node root,ControlsMapper controlsMapper,
            CameraManager cameraManager,TerrainManager terrainManager,GuiManager guiManager, 
            DayLightWeatherManager dayLightWeatherManager) {
        this.dataManager = dataManager;
        this.root = root;
        this.controlsMapper = controlsMapper;
        this.cameraManager = cameraManager;
        this.terrainManager = terrainManager;
        this.guiManager = guiManager;
        this.dayLightWeatherManager = dayLightWeatherManager;
                
    }

    public Mission mission(String missionName) {
        MissionDescriptor missionDescriptor = dataManager.missionDescriptor(missionName);
        List<Plane> planes = planes(missionDescriptor);
        Mission mission = new Mission(planes,dataManager.projectileManager(),dataManager.soundManager(),cameraManager,terrainManager,
                guiManager,dayLightWeatherManager);
        ControlsManager cm = new ControlsManager(controlsMapper, mission, cameraManager);
        return mission;
    }

    private List<Plane> planes(MissionDescriptor missionDescriptor) {
        List<Plane> planes = new ArrayList<>();
        for (PlaneMissionDescriptor planeMission : missionDescriptor.planeMissionDescriptors()) {
            Plane plane = dataManager.planeFactory().createPlane(planeMission.planeType());
            plane.setLocation(planeMission.startLocation());
            plane.planeMissinDescriptor(planeMission);
            plane.setThrottle(0.6f);
            root.attachChild(plane.getNode());
            planes.add(plane);
        }
        return planes;
    }
}
