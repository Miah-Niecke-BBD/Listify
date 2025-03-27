const API_BASE_URL = "http://localhost:8080";
import { jwtToken } from "@/models/JWTToken";
import type { Section } from "@/models/Section";
import { removeJwt } from "./Main";

export default class SectionsHandler {
  static async fetchSections(projectID: number): Promise<Section[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/projects/${projectID}/sections`, {
        method: "GET",
        headers: {
          Accept: "*/*",
          Authorization: `Bearer ${jwtToken}`,
        },
      });

      if (!response.ok) throw new Error("Failed to fetch sections");

      const sections = await response.json();
      return sections.map((section: any) => ({
        sectionID: section.sectionID,
        sectionName: section.sectionName,
        sectionPosition: section.sectionPosition,
        createdAt: new Date(section.createdAt),
        updatedAt: section.updatedAt ? new Date(section.updatedAt) : null,
      }));
    } catch (error:any) {
    
          if(error.status === 401){
                    removeJwt()
            }
      console.error("Error loading sections:", error);
      return [];
    }
  }

  static async createSection(projectID: number, sectionData: { sectionName: string; sectionPosition: number }): Promise<Section | null> {
    try {
      const response = await fetch(
        `${API_BASE_URL}/sections?projectID=${projectID}&sectionName=${encodeURIComponent(sectionData.sectionName)}&sectionPosition=${sectionData.sectionPosition}`,
        {
          method: "POST",
          headers: {
            Accept: "*/*",
            Authorization: `Bearer ${jwtToken}`,
          },
        }
      );

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to add section: ${errorMessage}`);
      }
      const createdSection = await response.json();
      return {
        sectionID: createdSection.sectionID,
        sectionName: createdSection.sectionName,
        sectionPosition: createdSection.sectionPosition,
        createdAt: new Date(createdSection.createdAt),
        updatedAt: createdSection.updatedAt ? new Date(createdSection.updatedAt) : null,
      };
    } catch (error:any) {

      if(error.status === 401){
                removeJwt()
        }

      console.error("Error adding section:", error);
      return null;
    }
  }

  static async updateSection(updatedSection: Partial<Section>): Promise<Section | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/sections/${updatedSection.sectionID}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        body: JSON.stringify(updatedSection),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to update section: ${errorMessage}`);
      }
      return await response.json();
    } catch (error:any) {

      if(error.status === 401){
                removeJwt()
        }

      console.error("Error updating section:", error);
      return null;
    }
  }

  static async updateSectionPosition(sectionID:number, newSectionPosition: number): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/sections/${sectionID}/position`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        body: JSON.stringify({ sectionPosition: newSectionPosition })
      })

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to update section position: ${errorMessage}`);
      }
      return true
    } catch (error) {
      console.error("Error updating section position", error)
      return false
    }
  }

  static async deleteSection(sectionID: number): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/sections/${sectionID}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      });
      if(response.status === 401){
        removeJwt()
        }
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to delete section: ${errorMessage}`);
      }

      return true;
    } catch (error:any) {

      console.error("Error deleting section:", error);
      return false;
    }
  }
}
